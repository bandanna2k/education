package dnt.websockets.client.vertx;

import dnt.websockets.client.ClientExecutionLayer;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.Requests;
import dnt.websockets.communications.*;
import dnt.websockets.server.vertx.VertxPublisher;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

public class VertxClient implements Requests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxClient.class);

    private final URI uri;
    private final MessageVisitor messageVisitor;
    private final Vertx vertx;

    private ClientExecutionLayer executorLayer;

    public VertxClient(Vertx vertx, String source, MessageVisitor messageVisitor)
    {
        this.vertx = vertx;
        this.uri = URI.create("/v1/websocket/").resolve(source);
        this.messageVisitor = messageVisitor;
    }

    public Future<WebSocket> run()
    {
        WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI(uri.toString())
                .setHost("localhost")
                .setPort(7777);
        options.setTimeout(3000);

        WebSocketClient wsClient = vertx.createWebSocketClient();
        return wsClient.connect(options)
                .onSuccess(this::handle)
                .onFailure(t -> LOGGER.error("Failed to start client.", t));
    }

    private void handle(WebSocket webSocket)
    {
        Publisher publisher = new VertxPublisher(webSocket);
        executorLayer = new ClientExecutionLayer(newExecutor(vertx), publisher);

        ClientTextMessageHandler messageHandler = new ClientTextMessageHandler(executorLayer, messageVisitor);
        webSocket.textMessageHandler(messageHandler);
    }

    @Override
    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return executorLayer.clientRequestFromServer(new GetPropertyRequest(key));
    }

    @Override
    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return executorLayer.clientRequestFromServer(new SetPropertyRequest(key, value));
    }

    private static VertxAsyncExecutor<AbstractResponse> newExecutor(Vertx vertx)
    {
        final VertxAsyncExecutor.UniqueIdGenerator uniqueIdGenerator = new VertxAsyncExecutor.UniqueIdGenerator()
        {
            private final AtomicLong nextCorrelationId = new AtomicLong(1);

            @Override
            public long generateId()
            {
                return nextCorrelationId.getAndIncrement();
            }
        };
        return new VertxAsyncExecutor<>(vertx, uniqueIdGenerator);
    }
}
