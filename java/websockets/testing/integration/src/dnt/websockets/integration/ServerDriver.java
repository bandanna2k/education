package dnt.websockets.integration;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.PushMessage;
import dnt.websockets.server.RequestProcessor;

public class ServerDriver
{
    private final IntegrationServer server;

    public ServerDriver(final ExecutionLayer executionLayer, RequestProcessor requestProcessor)
    {
        server = new IntegrationServer(executionLayer, requestProcessor);
    }

    public void broadcastMessage(final PushMessage message)
    {
        server.push(message);
    }

    public String getProperty(String key)
    {
        return server.getProperty(key);
    }
}
