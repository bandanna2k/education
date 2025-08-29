package dnt.websockets.integration;

import dnt.websockets.integration.eventbus.dsl.AbstractIntegrationEventBusTest;
import org.junit.Test;

public class IntegrationEventBusTest extends AbstractIntegrationEventBusTest
{
    @Test
    public void clientShouldRequestAndRespond()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectedValue: sam");
    }

    @Test
    public void serverShouldBroadcast()
    {
        client.verifyNoMoreMessages();

        server.broadcastMessage();

        client.verifyMessage("ServerPushMessage");
        client.verifyNoMoreMessages();
    }

    @Test
    public void serverShouldRequestRespond()
    {
        client.pushPulse("rate: 65", "sequence: 1");
    }
}
