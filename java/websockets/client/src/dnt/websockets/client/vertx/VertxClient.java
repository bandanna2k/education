package dnt.websockets.client.vertx;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.Requests;
import dnt.websockets.communications.*;
import dnt.websockets.server.vertx.VertxPublisher;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class VertxClient implements Requests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxClient.class);

    private final URI uri;
    private final PushMessageVisitor pushMessageVisitor;

    private Vertx vertx;
    private VertxClientExecutionLayer executorLayer;

    public VertxClient(Vertx vertx, String source, PushMessageVisitor pushMessageVisitor)
    {
        this.vertx = vertx;
        this.uri = URI.create("/v1/websocket/").resolve(source);
        this.pushMessageVisitor = pushMessageVisitor;
    }

    public Future<WebSocket> run()
    {
        HttpClient httpClient = vertx.createHttpClient();

        WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI(uri.toString())
                .setHost("localhost")
                .setPort(7777);
        options.setTimeout(3000);
        return httpClient.webSocket(options)
                .onSuccess(this::handle)
                .onFailure(t -> LOGGER.error("Failed to start client.", t));
    }

    private void handle(WebSocket webSocket)
    {
        Publisher publisher = new VertxPublisher(webSocket);
        executorLayer = new VertxClientExecutionLayer(vertx, publisher);

        ClientTextMessageHandler messageHandler = new ClientTextMessageHandler(executorLayer, pushMessageVisitor);
        webSocket.textMessageHandler(messageHandler);
    }

    @Override
    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return executorLayer.request(new GetPropertyRequest(key));
    }

    @Override
    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return executorLayer.request(new SetPropertyRequest(key, value));
    }
}
