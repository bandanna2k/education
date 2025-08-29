package dnt.websockets.integration.eventbus.dsl;

import dnt.websockets.integration.eventbus.EventBusServerDriver;

public class EventBusServerDsl
{
    private final EventBusServerDriver serverDriver;

    public EventBusServerDsl(EventBusServerDriver serverDriver)
    {
        this.serverDriver = serverDriver;
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
    }
}
