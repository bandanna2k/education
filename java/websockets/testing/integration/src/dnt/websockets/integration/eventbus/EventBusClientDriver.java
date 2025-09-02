package dnt.websockets.integration.eventbus;

import dnt.websockets.client.eventbus.EventBusClient;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.GetPropertyResponse;
import dnt.websockets.messages.SetPropertyResponse;
import dnt.websockets.integration.MessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import static dnt.websockets.messages.MessageVisitor.NO_OP;

public class EventBusClientDriver
{
    private final EventBusClient client;
    private final MessageCollector clientMessageCollector = new MessageCollector(this.getClass().getSimpleName(), NO_OP);

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

    public void pushPulse(int rate, long sequence)
    {
        client.pushPulse(rate, sequence);
    }
}
