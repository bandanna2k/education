package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.integration.vertx.ServerVertxDriver;

public class ServerVertxDsl
{
    private final ServerVertxDriver serverDriver;

    public ServerVertxDsl(ServerVertxDriver serverDriver)
    {
        this.serverDriver = serverDriver;
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
    }
}
