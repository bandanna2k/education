package dnt.websockets.integration;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.messages.*;
import dnt.websockets.server.ServerMessageProcessor;
import education.common.result.Result;
import io.vertx.core.Future;

public class ServerDriver
{
    private static int nextCorrelationId = 1;

    private final ExecutionLayer executionLayer;
    private final ServerMessageProcessor messageProcessor;

    public ServerDriver(final ExecutionLayer executionLayer, ServerMessageProcessor messageProcessor)
    {
        this.executionLayer = executionLayer;
        this.messageProcessor = messageProcessor;
    }

    public void broadcastMessage(final ServerPushMessage message)
    {
        executionLayer.serverSend(message);
    }

    public String getProperty(String key)
    {
        return messageProcessor.get(key);
    }

    public Future<Result<GetStatusResponse, String>> getStatusFromClient(String client)
    {
        return request(new GetStatusRequest(client));
    }

    private <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request)
    {
        return executionLayer.serverRequestOnClient(request.attachCorrelationId(nextCorrelationId++));
    }
}
