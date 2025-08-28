package dnt.websockets.integration;

import dnt.websockets.communications.*;
import dnt.websockets.server.ServerMessageProcessor;
import education.common.result.Result;
import io.vertx.core.Future;

public class ServerDriver
{
    private final IntegrationServer server;
    private final ExecutionLayer executionLayer;
    private final ServerMessageProcessor messageProcessor;

    public ServerDriver(final ExecutionLayer executionLayer, ServerMessageProcessor messageProcessor)
    {
        this.executionLayer = executionLayer;
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

    public Future<Result<GetStatusResponse, String>> getStatusFromClient(String client)
    {
        return executionLayer.serverRequestFromClient(new GetStatusRequest(client));
    }
}
