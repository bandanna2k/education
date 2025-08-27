package dnt.websockets.integration;

import dnt.websockets.integration.dsl.AbstractIntegrationTest;
import org.junit.Test;

public class IntegrationTest extends AbstractIntegrationTest
{
    @Test
    public void clientShouldRequestAndResponse()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectedValue: sam");
    }

    @Test
    public void serverShouldBroadcastMessage()
    {
        client.verifyNoMoreMessages();

        server.broadcastMessage();

        client.verifyMessage("PushMessage");
    }

    @Test
    public void serverShouldFutureFailNextMessage()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectException: false");

        integration.throwOnNextMessage();

        client.getProperty("key: name", "expectException: true");
    }

    @Test
    public void shouldFailIfNoResponse()
    {
        client.setProperty("key: do_not_send_response", "value: true",
                "expectSuccess: false", "expectedErrorMessage: No response received");
    }

    @Test
    public void serverShouldFailNextMessage()
    {
        client.setProperty("key: name", "value: sam", "expectSuccess: true");

        integration.failNextMessage("Not available for this user.");

        client.setProperty("key: name", "value: sam", "expectSuccess: false");
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
    public void shouldSupportMultipleClients()
    {
        client("session1").setProperty("key: name", "value: sam", "expectSuccess: true");

        // Both clients see the broadcasted message.
        client("session1").verifyMessage("SetPropertyResponse");
        client("session2").verifyMessage("SetPropertyResponse");
    }

    @Test
    public void shouldNotAcceptEmptyValue()
    {
        client.setProperty("key: name", "value: ",
                "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
    }

    @Test
    public void shouldNotAcceptEmptyKey()
    {
        client.setProperty("key: ", "value: sam",
                "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
    }

    @Test
    public void shouldNotAcceptNullValue()
    {
        client.setProperty("key: name", "value: <NULL>",
                "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
    }

    @Test
    public void shouldNotAcceptNullKey()
    {
        client.setProperty("key: <NULL>", "value: sam",
                "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
    }
}
