package dnt.websockets.client.websocket;

import dnt.websockets.client.ClientExecutionLayer;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.Requests;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.*;
import dnt.websockets.server.vertx.WebSocketPublisher;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

public class WebSocketKlient implements Requests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketKlient.class);

    private final URI uri;
    private final MessageVisitor messageProcessor;
    private final Vertx vertx;

    private ClientExecutionLayer executorLayer;

    public WebSocketKlient(Vertx vertx, String source, MessageVisitor messageProcessor)
    {
        this.vertx = vertx;
        this.uri = URI.create("/v1/websocket/").resolve(source);
        this.messageProcessor = messageProcessor;
    }

    public Future<WebSocket> run()
    {
        final WebSocketConnectOptions connectOptions = new WebSocketConnectOptions()
                .setURI(uri.toString())
                .setHost("localhost")
                .setPort(7777);

        LOGGER.info("Attempting connection. {}", connectOptions);

        final WebSocketClientOptions webSocketClientOptions = new WebSocketClientOptions();
//                .setTcpNoDelay(true)
//                .setIdleTimeout(0)
//                .setConnectTimeout(10_000)
//                .setMaxConnections(1)
//                .setTcpKeepAlive(true);
        final WebSocketClient wsClient = vertx.createWebSocketClient(webSocketClientOptions);
        return wsClient.connect(connectOptions)
                .onSuccess(this::handle)
                .onFailure(t -> {
                    LOGGER.error("Failed to start client.", t);
                });
    }

    private void handle(WebSocket webSocket)
    {
        Publisher publisher = new WebSocketPublisher(webSocket);
        executorLayer = new ClientExecutionLayer(newExecutor(vertx), publisher);

        ClientTextMessageHandler messageHandler = new ClientTextMessageHandler(executorLayer, messageProcessor);
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
