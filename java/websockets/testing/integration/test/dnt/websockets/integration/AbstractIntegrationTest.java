package dnt.websockets.integration;

import dnt.websockets.integration.dsl.IntegrationDsl;

public abstract class AbstractIntegrationTest
{
    protected final IntegrationDsl client = new IntegrationDsl();
}
