package dnt.websockets.integration.dsl;

import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.server.RequestProcessor;

public abstract class AbstractIntegrationTest
{
    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(RequestProcessor::new);

    protected final ClientDsl client = new ClientDsl(executionLayer);
    protected final ServerDsl server = new ServerDsl(executionLayer);
}
