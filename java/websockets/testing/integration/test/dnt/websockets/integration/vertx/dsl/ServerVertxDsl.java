package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.integration.vertx.WebSocketServerDriver;

public class ServerVertxDsl
{
    private final WebSocketServerDriver serverDriver;

    public ServerVertxDsl(WebSocketServerDriver serverDriver)
    {
        this.serverDriver = serverDriver;
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
    }
}
