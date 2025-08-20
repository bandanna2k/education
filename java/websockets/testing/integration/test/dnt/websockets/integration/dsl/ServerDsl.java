package dnt.websockets.integration.dsl;

import dnt.websockets.client.ExecutionLayer;
import dnt.websockets.integration.ServerDriver;

public class ServerDsl
{
    private final ServerDriver serverDriver;

    public ServerDsl(ExecutionLayer executionLayer)
    {
        serverDriver = new ServerDriver(executionLayer);
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
    }
}
