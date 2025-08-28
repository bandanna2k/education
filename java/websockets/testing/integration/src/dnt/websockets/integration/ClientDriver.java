package dnt.websockets.integration;

import dnt.websockets.client.Requests;
import dnt.websockets.communications.*;
import education.common.result.Result;
import io.vertx.core.Future;

public class ClientDriver implements Requests
{
    private final ExecutionLayer executionLayer;

    public ClientDriver(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
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

    public void pushPrice(String symbol, double price, long sequence)
    {
        executionLayer.clientSend(new ClientPushPrice(symbol, price, sequence));
    }
}
