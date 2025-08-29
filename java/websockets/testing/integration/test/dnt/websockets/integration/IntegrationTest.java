package dnt.websockets.integration;

import dnt.websockets.integration.base.AbstractIntegrationTest;
import org.junit.Test;

/*
 Example tests. See ToClientTests and ToServerTests for more
 */
public class IntegrationTest extends AbstractIntegrationTest
{
    @Test
    public void clientShouldRequestAndResponse()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectedValue: sam");
    }

    @Test
    public void clientShouldPushMessage()
    {
        client.pushPulse("rate: 60", "sequence: 1");
        server.verifyMessage("ClientPushPulse");
    }

    @Test
    public void shouldPauseProcessing()
    {
        client.setProperty("key: name", "value: alex", "complete: true");

        integration.pauseProcessing();
        client.setProperty("key: name", "value: drew", "complete: false");
        client.setProperty("key: name", "value: riley", "complete: false");
        client.setProperty("key: name", "value: sam", "complete: false");
        client.setProperty("key: name", "value: terry", "complete: false");

        server.verifyProperty("key: name", "expectedValue: alex");

        integration.resumeProcessing("messageCount: 1");
        server.verifyProperty("key: name", "expectedValue: drew");

        integration.resumeProcessing();
        server.verifyProperty("key: name", "expectedValue: terry");
    }

    @Test
    public void serverShouldRequestAndResponse()
    {
        server.getStatusFromClient("client: session1", "expectedStatus: Wicked");
    }

    @Test
    public void serverShouldPushMessage()
    {
        client.verifyNoMoreMessages();
        server.broadcastMessage();
        client.verifyMessage("ServerPushMessage");
    }
}
