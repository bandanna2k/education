package dnt.websockets.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dnt.websockets.client.requests.Requests;
import dnt.websockets.server.infrastructure.MessagePublisher;
import dnt.websockets.server.infrastructure.AbstractRequest;
import dnt.websockets.server.infrastructure.OptionsRequest;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketClientOptions;
import io.vertx.core.http.WebSocketConnectOptions;

import static dnt.websockets.VertxFactory.newVertx;

public class Client
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(AbstractRequest.class);

    private Requests requests;
    private int correlationId = 1;
    private Vertx vertx;

    public Future<WebSocket> go()
    {
        vertx = newVertx();
        HttpClient httpClient = vertx.createHttpClient();

        WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI("v1/websocket")
                .setHost("localhost")
                .setPort(7777);
        return httpClient.webSocket(options)
                .onSuccess(webSocket -> {
                    MessagePublisher messagePublisher = new MessagePublisher(webSocket, OBJECT_MAPPER);
                    requests = correlationId -> messagePublisher.send(new OptionsRequest(messagePublisher.getNextCorrelationId()));
                    ClientWebSocketTextMessageHandler messageHandler = new ClientWebSocketTextMessageHandler(MESSAGE_READER, messagePublisher);
                    webSocket.textMessageHandler(messageHandler);
                });
    }

    public void requestOptions()
    {
        requests.requestOptions(correlationId++);
    }

    public void close()
    {
        vertx.close();
    }
}
