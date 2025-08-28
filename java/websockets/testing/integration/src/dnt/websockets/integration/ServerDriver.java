package dnt.websockets.integration;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.ServerPushMessage;
import dnt.websockets.server.ServerMessageProcessor;

public class ServerDriver
{
    private final IntegrationServer server;
    private final ServerMessageProcessor messageProcessor;

    public ServerDriver(final ExecutionLayer executionLayer, ServerMessageProcessor messageProcessor)
    {
        this.messageProcessor = messageProcessor;
        this.server = new IntegrationServer(executionLayer, messageProcessor);
    }

    public void broadcastMessage(final ServerPushMessage message)
    {
        server.push(message);
    }

    public String getProperty(String key)
    {
        return messageProcessor.get(key);
    }
}
