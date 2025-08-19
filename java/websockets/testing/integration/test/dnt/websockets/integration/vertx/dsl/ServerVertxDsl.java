package dnt.websockets.integration.vertx.dsl;

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
