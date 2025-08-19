package dnt.websockets.integration.dsl;

import dnt.websockets.client.ExecutionLayer;
import dnt.websockets.communications.AbstractResponse;
import dnt.websockets.communications.OptionsRequest;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.server.RequestProcessor;
import education.common.result.Result;
import io.vertx.core.Future;

public class ClientDriver
{
    private final ExecutionLayer executionLayer = new IntegrationExecutionLayer(RequestProcessor::new);

    public Future<Result<OptionsResponse, String>> fetchOptions()
    {
        return executionLayer.send(new OptionsRequest());
    }
}
