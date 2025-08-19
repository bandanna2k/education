package dnt.websockets.integration;

import dnt.websockets.integration.dsl.ClientDsl;

public abstract class AbstractIntegrationTest
{
    protected final ClientDsl client = new ClientDsl();
}
