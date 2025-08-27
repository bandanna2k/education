package dnt.websockets.integration.also;

import dnt.websockets.integration.also.dsl.AbstractIntegrationEventBusTest;
import org.junit.Test;

public class IntegrationEventBusTest extends AbstractIntegrationEventBusTest
{
    @Test
    public void shouldSendAndReceive()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectedValue: sam");
    }

    @Test
    public void serverShouldBroadcast()
    {
        client.verifyNoMoreMessages();

        server.broadcastMessage();

        client.verifyMessage("PushMessage");
        client.verifyNoMoreMessages();
    }
}
