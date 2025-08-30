package dnt.websockets.integration.vertx;

import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.client.websocket.WebSocketKlient;
import dnt.websockets.integration.MessageCollector;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.ClientPushPulse;
import dnt.websockets.messages.GetPropertyResponse;
import dnt.websockets.messages.SetPropertyResponse;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;

public class WebSocketClientDriver
{
    private final WebSocketKlient client;
    private final ClientMessageProcessor clientMessageProcessor = new ClientMessageProcessor();
    private final MessageCollector collector = new MessageCollector(this.getClass().getSimpleName(), clientMessageProcessor);
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

    public void setStatus(String status)
    {
        clientMessageProcessor.setStatus(status);
    }

    public void pushPulse(int rate, long sequence)
    {
        client.pushMessage(new ClientPushPulse(rate, sequence));
    }
}
