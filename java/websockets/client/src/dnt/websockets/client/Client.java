package dnt.websockets.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dnt.websockets.communications.AbstractRequest;
import dnt.websockets.communications.AbstractResponse;
import dnt.websockets.communications.MessagePublisher;
import dnt.websockets.communications.OptionsRequest;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public class Client
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(AbstractResponse.class);

    private Vertx vertx;
    private MessagePublisher messagePublisher;

    public Future<WebSocket> run()
    {
        vertx = newVertx();
        HttpClient httpClient = vertx.createHttpClient();

        WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI("/v1/websocket")
                .setHost("localhost")
                .setPort(7777)
                .setTimeout(3000);
        return httpClient.webSocket(options)
                .onSuccess(webSocket -> {
                    messagePublisher = new MessagePublisher(webSocket, OBJECT_MAPPER);
                    WebSocketTextMessageHandler messageHandler = new WebSocketTextMessageHandler(MESSAGE_READER);
                    webSocket.textMessageHandler(messageHandler);
                })
                .onFailure(t -> LOGGER.error("Failed to start client.", t));
    }

    public void requestOptions()
    {
        messagePublisher.send(new OptionsRequest(messagePublisher.getNextCorrelationId()));
    }

    public void close()
    {
        vertx.close();
    }
}
