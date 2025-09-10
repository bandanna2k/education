package dnt.websockets.integration;

import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.client.ClientRequests;
import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.messages.*;
import education.common.result.Result;
import io.vertx.core.Future;

public class ClientDriver implements ClientRequests
{
    private final ExecutionLayer executionLayer;
    private final ClientMessageProcessor clientMessageProcessor;

    public ClientDriver(ExecutionLayer executionLayer, ClientMessageProcessor clientMessageProcessor)
    {
        this.executionLayer = executionLayer;
        this.clientMessageProcessor = clientMessageProcessor;
    }

    @Override
    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return executionLayer.clientRequestFromServer(new GetPropertyRequest(key));
    }

    @Override
    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return executionLayer.clientRequestFromServer(new SetPropertyRequest(key, value));
    }

    public void pushPulse(int rate, long sequence)
    {
        executionLayer.clientSend(new ClientPushPulse(rate, sequence));
    }

    public void setStatus(String status)
    {
        clientMessageProcessor.setStatus(status);
    }
}
