package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.integration.vertx.VertxServerDriver;

public class ServerVertxDsl
{
    private final VertxServerDriver serverDriver;

    public ServerVertxDsl(VertxServerDriver serverDriver)
    {
        this.serverDriver = serverDriver;
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
    }
}
