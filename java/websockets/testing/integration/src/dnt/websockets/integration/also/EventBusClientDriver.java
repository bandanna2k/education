package dnt.websockets.integration.also;

import dnt.websockets.client.also.EventBusClient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyResponse;
import dnt.websockets.integration.MessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class EventBusClientDriver
{
    private final EventBusClient client;
    private final MessageCollector clientMessageCollector = new MessageCollector();

    public EventBusClientDriver(Vertx vertx)
    {
        client = new EventBusClient(vertx, clientMessageCollector);
        client.start();
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
        return clientMessageCollector.getLastMessage();
    }

    public void pushPrice(String symbol, double price, long sequence)
    {
        client.pushPrice(symbol, price, sequence);
    }
}
