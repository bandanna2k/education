package dnt.websockets.integration.also;

import dnt.websockets.communications.PushMessage;
import dnt.websockets.server.also.EventBusServer;
import io.vertx.core.Vertx;

public class EventBusServerDriver
{
    private final EventBusServer server;

    public EventBusServerDriver(Vertx vertx)
    {
        server = new EventBusServer(vertx);
        server.start();
    }

    public void broadcastMessage()
    {
        server.broadcast(new PushMessage());
    }
}
