package dnt.websockets.integration.maybecool;

import dnt.websockets.integration.maybecool.dsl.AbstractIntegrationTcpTest;
import org.junit.Test;

public class IntegrationTcpTest extends AbstractIntegrationTcpTest
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
        server.broadcastMessage();

        client.verifyMessage("PushMessage");
    }
}
