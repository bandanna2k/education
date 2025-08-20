package dnt.websockets.integration;

import dnt.websockets.client.ExecutionLayer;
import dnt.websockets.communications.PushMessage;

public class ServerDriver
{
    private final ExecutionLayer executionLayer;

    public ServerDriver(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    public void broadcastMessage()
    {
        executionLayer.broadcastServerToClient(new PushMessage());
    }
}
