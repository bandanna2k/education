package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsRequest;
import dnt.websockets.communications.OptionsResponse;
import education.common.result.Result;
import io.vertx.core.Future;

public class ClientDriver
{
    private final IntegrationExecutionLayer executionLayer;

    public ClientDriver(IntegrationExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    public Future<Result<OptionsResponse, String>> fetchOptions()
    {
        return executionLayer.sendClientToServer(new OptionsRequest());
    }

    public AbstractMessage getLastMessage()
    {
        return executionLayer.getLastMessage();
    }
}
