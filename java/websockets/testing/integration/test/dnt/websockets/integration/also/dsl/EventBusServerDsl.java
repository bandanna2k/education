package dnt.websockets.integration.also.dsl;

import dnt.websockets.integration.also.EventBusServerDriver;

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
