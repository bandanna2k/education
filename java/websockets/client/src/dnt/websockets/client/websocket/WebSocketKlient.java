package dnt.websockets.client.websocket;

import dnt.websockets.client.ClientExecutionLayer;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.Requests;
import dnt.websockets.communications.*;
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
        final HttpClientOptions clientOptions = new HttpClientOptions()
                .setTcpKeepAlive(true)
                .setIdleTimeout(0) // Disable idle timeout
                .setTcpNoDelay(true)
                .setConnectTimeout(10000)
//                .setMaxPoolSize(1)
                .setKeepAlive(true);
        HttpClient httpClient = vertx.createHttpClient();

        final WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI(uri.toString())
                .setHost("localhost")
                .setPort(7777);
//                .addHeader("Connection", "Upgrade")
//                .addHeader("Upgrade", "websocket");

        LOGGER.info("Attempting connection. {}", options);

        final WebSocketClient wsClient = vertx.createWebSocketClient();
        return wsClient.connect(options)
                .onSuccess(this::handle)
                .onFailure(t -> LOGGER.error("Failed to start client.", t));
    }

    private void handle(WebSocket webSocket)
    {
        System.out.println("1");
        Publisher publisher = new WebSocketPublisher(webSocket);
        System.out.println("2");
        executorLayer = new ClientExecutionLayer(newExecutor(vertx), publisher);

        System.out.println("3");
        ClientTextMessageHandler messageHandler = new ClientTextMessageHandler(executorLayer, messageProcessor);
        System.out.println("4");
        webSocket.textMessageHandler(messageHandler);
        System.out.println("5");
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
