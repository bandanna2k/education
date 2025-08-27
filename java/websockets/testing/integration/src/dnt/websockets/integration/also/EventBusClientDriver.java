package dnt.websockets.integration.also;

import dnt.websockets.client.also.EventBusClient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyResponse;
import dnt.websockets.integration.PushMessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class EventBusClientDriver
{
    private final EventBusClient client;
    private final PushMessageCollector collector = new PushMessageCollector();

    public EventBusClientDriver(Vertx vertx)
    {
        client = new EventBusClient(vertx, collector);
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
        return collector.getLastMessage();
    }
}
