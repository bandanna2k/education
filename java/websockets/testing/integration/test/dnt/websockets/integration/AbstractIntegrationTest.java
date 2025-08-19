package dnt.websockets.integration;

import dnt.websockets.integration.dsl.ClientDsl;
import dnt.websockets.integration.dsl.ServerDsl;
import dnt.websockets.server.RequestProcessor;

public abstract class AbstractIntegrationTest
{
    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(RequestProcessor::new);

    protected final ClientDsl client = new ClientDsl(executionLayer);
    protected final ServerDsl server = new ServerDsl(executionLayer);
}
