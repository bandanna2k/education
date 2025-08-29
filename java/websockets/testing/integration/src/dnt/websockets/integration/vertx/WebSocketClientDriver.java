package dnt.websockets.integration.vertx;

import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.client.websocket.WebSocketKlient;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.GetPropertyResponse;
import dnt.websockets.messages.SetPropertyResponse;
import dnt.websockets.integration.MessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketClientDriver
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientDriver.class);

    private final WebSocketKlient client;
    private final MessageCollector collector = new MessageCollector(this.getClass().getSimpleName(),
            new ClientMessageProcessor());
    private final String clientId;

    public WebSocketClientDriver(Vertx vertx, String clientId)
    {
        this.clientId = clientId;
        this.client = new WebSocketKlient(vertx, clientId, collector);
    }

    public Future<WebSocket> start()
    {
        return this.client.run();
    }

    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return client.setProperty(key, value)
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
    }

    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return client.getProperty(key)
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
    }

    public AbstractMessage popLastMessage()
    {
        return collector.getLastMessage();
    }

    public String getClientId()
    {
        return clientId;
    }
}
