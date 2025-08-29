package dnt.websockets.server.vertx;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.GetPropertyRequest;
import dnt.websockets.messages.SetPropertyRequest;
import dnt.websockets.server.ServerMessageProcessor;
import dnt.websockets.server.ServerExecutionLayer;
import dnt.websockets.server.ServerTextMessageHandler;
import dnt.websockets.vertx.VertxAsyncExecutor;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebSocketServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_CONNECT = 100;

    private final List<ServerTextMessageHandler> textMessageHandlers = new ArrayList<>();
    private final Vertx vertx;
    private final ServerMessageProcessor requestProcessor = new ServerMessageProcessor();

    public WebSocketServer(Vertx vertx)
    {
        this.vertx = vertx;
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
        URI uri = URI.create(serverWebSocket.path());
        if (!uri.toString().contains("/v1/websocket"))
        {
            LOGGER.warn("Failed to connect websocket");
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_CONNECT);
            return;
        }

        LOGGER.info("Websocket connected {}", uri);

        final Publisher publisher = new WebSocketPublisher(serverWebSocket);
        final ExecutionLayer executionLayer = new ServerExecutionLayer(VertxAsyncExecutor.newExecutor(vertx), publisher);
        final ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executionLayer, requestProcessor);
        serverWebSocket.textMessageHandler(textMessageHandler);
        textMessageHandlers.add(textMessageHandler);
    }

    public void broadcast(AbstractMessage message)
    {
        Iterator<ServerTextMessageHandler> iterator = textMessageHandlers.iterator();
        while (iterator.hasNext())
        {
            ServerTextMessageHandler next;
            try
            {
                next = iterator.next();
                next.send(message);
            }
            catch (Exception e)
            {
                iterator.remove();
                LOGGER.error("Error writing message", e);
            }
        }
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
        final ServerExecutionLayer restExecutionLayer = new ServerExecutionLayer(VertxAsyncExecutor.newExecutor(vertx), restPublisher);
        restPublisher.publisher = new RestPublisher(ctx);
        return new ServerTextMessageHandler(restExecutionLayer, requestProcessor);
    }
}
