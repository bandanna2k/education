package dnt.websockets.server.vertx;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.*;
import dnt.websockets.server.ServerExecutionLayer;
import dnt.websockets.server.ServerRequests;
import dnt.websockets.server.ServerTextMessageHandler;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebSocketServer implements ServerRequests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_CONNECT = 100;

    private final Map<String, ExecutionLayer> executionLayers = new HashMap<>();
    private final Vertx vertx;
    private final MessageVisitor messageProcessor;

    public WebSocketServer(Vertx vertx, MessageVisitor messageProcessor)
    {
        this.vertx = vertx;
        this.messageProcessor = messageProcessor;
    }

    public Future<HttpServer> start()
    {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/property").handler(this::restGetProperty);
        router.post("/property").handler(this::restSetProperty);

        return vertx.createHttpServer()
                .requestHandler(router)
                .webSocketHandler(this::handle)
                .listen(7780)
                .onSuccess(successfulHttpServer -> {
                    LOGGER.info("Server started on port {}", successfulHttpServer.actualPort());
                })
                .onFailure(t -> LOGGER.error("Failed to start server", t));
    }

    private void handle(ServerWebSocket serverWebSocket)
    {
        String path = serverWebSocket.path();
        URI uri = URI.create(path);
        String clientId = getClientId(path);
        if (!uri.toString().startsWith("/v1/websocket/") || null == clientId)
        {
            LOGGER.warn("Failed to connect websocket");
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_CONNECT);
            return;
        }

        LOGGER.info("Websocket connected {}", uri);

        final Publisher publisher = new WebSocketPublisher(serverWebSocket);
        final VertxAsyncExecutor<AbstractResponse> executor = new VertxAsyncExecutor.Builder(vertx).timeoutMillis(2_000L).build();
        final ExecutionLayer executionLayer = new ServerExecutionLayer(executor, publisher);
        final ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executionLayer, messageProcessor);
        serverWebSocket.textMessageHandler(textMessageHandler);
        executionLayers.put(clientId, executionLayer);
    }

    private static String getClientId(String path)
    {
        int index = path.lastIndexOf("/");
        return index > 0 ? path.substring(index + 1) : null;
    }

    public void broadcast(AbstractMessage message)
    {
        Iterator<ExecutionLayer> iterator = executionLayers.values().iterator();
        while (iterator.hasNext())
        {
            ExecutionLayer next;
            try
            {
                next = iterator.next();
                next.serverSend(message);
            }
            catch (Exception e)
            {
                iterator.remove();
                LOGGER.error("Error writing message", e);
            }
        }
    }

    @Override
    public Future<Result<GetStatusResponse, String>> getStatus(String clientId)
    {
        return executionLayers.get(clientId).serverRequestOnClient(new GetStatusRequest(clientId));
    }

    private static class LazyPublisher implements Publisher
    {
        Publisher publisher;

        @Override
        public void send(AbstractMessage message)
        {
            publisher.send(message);
        }
    }



    private void restGetProperty(RoutingContext ctx)
    {
        final ServerTextMessageHandler restServerTextMessageHandler = newRestTextMessageHandler(ctx);
        String key = ctx.queryParams().get("key");
        restServerTextMessageHandler.handle(new GetPropertyRequest(key));
    }
    private void restSetProperty(RoutingContext ctx)
    {
        final ServerTextMessageHandler restServerTextMessageHandler = newRestTextMessageHandler(ctx);
        JsonObject json = ctx.body().asJsonObject();
        restServerTextMessageHandler.handle(new SetPropertyRequest(json.getString("key"), json.getString("value")));
    }
    private ServerTextMessageHandler newRestTextMessageHandler(RoutingContext ctx)
    {
        final LazyPublisher restPublisher = new LazyPublisher();
        VertxAsyncExecutor<AbstractResponse> executor = new VertxAsyncExecutor.Builder(vertx).build();
        final ServerExecutionLayer restExecutionLayer = new ServerExecutionLayer(executor, restPublisher);
        restPublisher.publisher = new RestPublisher(ctx);
        return new ServerTextMessageHandler(restExecutionLayer, messageProcessor);
    }
}
