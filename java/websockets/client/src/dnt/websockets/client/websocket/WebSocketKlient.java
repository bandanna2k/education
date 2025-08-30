package dnt.websockets.client.websocket;

import dnt.websockets.client.ClientExecutionLayer;
import dnt.websockets.client.ClientRequests;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.*;
import dnt.websockets.server.vertx.WebSocketPublisher;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketClient;
import io.vertx.core.http.WebSocketConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static dnt.websockets.vertx.VertxAsyncExecutor.newExecutor;

public class WebSocketKlient implements ClientRequests
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
                .setIdleTimeout(10_000)
                .setURI(uri.toString())
                .setHost("localhost")
                .setPort(7780);

        LOGGER.info("Attempting connection. {}", connectOptions);

        final WebSocketClient wsClient = vertx.createWebSocketClient();

        final int createWsClientGarbageCollectionDelay = 100;
        return delay(vertx, createWsClientGarbageCollectionDelay)
                .flatMap(unused -> wsClient
                        .connect(connectOptions)
                        .timeout(5, TimeUnit.SECONDS)
                        .onSuccess(this::handle)
                        .onFailure(t -> LOGGER.error("Failed to start client.", t)));
    }

    private static Future<Void> delay(Vertx vertx, long delayMs)
    {
        Promise<Void> promise = Promise.promise();
        vertx.setTimer(delayMs, id -> promise.complete());
        return promise.future();
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

    public void pushMessage(ClientPushPulse clientPushPulse)
    {
        executorLayer.clientSend(clientPushPulse);
    }
}
