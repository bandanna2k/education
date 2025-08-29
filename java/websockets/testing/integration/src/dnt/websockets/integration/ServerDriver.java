package dnt.websockets.integration;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.integration.infrastructure.IntegrationServerConnector;
import dnt.websockets.messages.GetStatusRequest;
import dnt.websockets.messages.GetStatusResponse;
import dnt.websockets.messages.ServerPushMessage;
import dnt.websockets.server.ServerMessageProcessor;
import education.common.result.Result;
import io.vertx.core.Future;

public class ServerDriver
{
    private final IntegrationServerConnector server;
    private final ExecutionLayer executionLayer;
    private final ServerMessageProcessor messageProcessor;

    public ServerDriver(final ExecutionLayer executionLayer, ServerMessageProcessor messageProcessor)
    {
        this.executionLayer = executionLayer;
        this.messageProcessor = messageProcessor;
        this.server = new IntegrationServerConnector(executionLayer, messageProcessor);
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
        return executionLayer.serverRequestOnClient(new GetStatusRequest(client));
    }
}
