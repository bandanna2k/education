package dnt.websockets.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import dnt.websockets.communications.*;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public class Client implements Requests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private final ObjectMapper mapper;
    private final ObjectReader messageReader;
    private final Queue<AbstractMessage> broadcastMessages = new LinkedList<>();

    private Vertx vertx;
    private WebSocketExecutorLayer executorLayer;

    public Client()
    {
        mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.registerSubtypes(new NamedType(OptionsResponse.class, OptionsResponse.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(PushMessage.class, PushMessage.class.getSimpleName()));
        messageReader = mapper.readerFor(AbstractMessage.class);
    }

    public Future<WebSocket> run()
    {
        vertx = newVertx();
        HttpClient httpClient = vertx.createHttpClient();

        WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI("/v1/websocket")
                .setHost("localhost")
                .setPort(7777);
        options.setTimeout(3000);
        return httpClient.webSocket(options)
                .onSuccess(webSocket -> {
                    MessagePublisher messagePublisher = new MessagePublisher(webSocket, mapper);
                    executorLayer = new WebSocketExecutorLayer(vertx, messagePublisher);

                    WebSocketTextMessageHandler messageHandler = new WebSocketTextMessageHandler(messageReader, executorLayer, broadcastMessages::add);
                    webSocket.textMessageHandler(messageHandler);
                })
                .onFailure(t -> LOGGER.error("Failed to start client.", t));
    }

    @Override
    public Future<Result<OptionsResponse, String>> requestOptions()
    {
        return executorLayer.send(new OptionsRequest());
    }

    public AbstractMessage popLastMessage()
    {
        return broadcastMessages.poll();
    }
}
