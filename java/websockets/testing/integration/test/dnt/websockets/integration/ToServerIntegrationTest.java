package dnt.websockets.integration;

import dnt.websockets.integration.base.AbstractIntegrationTest;
import dnt.websockets.integration.base.ToServerTests;
import org.junit.Test;

public class ToServerIntegrationTest extends AbstractIntegrationTest implements ToServerTests
{
    @Test
    @Override
    public void clientShouldRequestAndResponse()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectedValue: sam");
    }

    @Test
    @Override
    public void clientShouldPushMessage()
    {
        client.pushPulse("rate: 60", "sequence: 1");
        server.verifyMessage("ClientPushPulse");
    }

    @Test
    @Override
    public void serverShouldFutureFailNextMessage()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectException: false");

        integration.throwOnNextMessage();

        client.getProperty("key: name", "expectException: true");
    }

    @Test
    @Override
    public void shouldFailIfNoResponse()
    {
        client.setProperty("key: do_not_send_response", "value: true",
                "expectSuccess: false", "expectedErrorMessage: No response received");
    }

    @Test
    @Override
    public void serverShouldFailNextMessage()
    {
        client.setProperty("key: name", "value: sam", "expectSuccess: true");

        integration.failNextMessage("Not available for this user.");

        client.setProperty("key: name", "value: sam", "expectSuccess: false");
    }

    @Test
    @Override
    public void shouldSupportMultipleClients()
    {
        client("session1").setProperty("key: name", "value: sam", "expectSuccess: true");

        // Both clients see the broadcasted message.
        client("session1").verifyMessage("SetPropertyResponse");
        client("session2").verifyNoMoreMessages();
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
