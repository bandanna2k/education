package dnt.websockets.integration.vertx;

import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.client.websocket.WebSocketKlient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyResponse;
import dnt.websockets.integration.MessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class WebSocketClientDriver
{
    private final WebSocketKlient client;
    private final MessageCollector collector = new MessageCollector(this.getClass().getSimpleName(),
            new ClientMessageProcessor());

    public WebSocketClientDriver(Vertx vertx, String source)
    {
        client = new WebSocketKlient(vertx, source, collector);
        client.run()
                .onFailure(throwable ->
                {
                    throw new RuntimeException(throwable);
                })
                .toCompletionStage().toCompletableFuture().join();
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
