package dnt.websockets.integration.eventbus;

import dnt.websockets.messages.ServerPushMessage;
import dnt.websockets.server.eventbus.EventBusServer;
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
        server.broadcast(new ServerPushMessage());
    }
}
