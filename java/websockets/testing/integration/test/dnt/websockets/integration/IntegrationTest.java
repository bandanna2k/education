package dnt.websockets.integration;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.integration.dsl.AbstractIntegrationTest;
import dnt.websockets.server.ServerTextMessageHandler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;

import static dnt.websockets.client.ClientTextMessageHandler.OBJECT_MAPPER;

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
        client.setProperty("key: do_not_send_response", "value: true", "expectSuccess: false");
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
    public void shouldNotAcceptEmptyValue()
    {
        client.setProperty("key: name", "value: ", "expectSuccess: false");
    }

    @Test
    public void shouldNotAcceptEmptyKey()
    {
        client.setProperty("key: ", "value: sam", "expectSuccess: false");
    }

    @Test
    public void shouldNotAcceptNullValue()
    {
        client.setProperty("key: name", "value: <NULL>", "expectSuccess: false");
    }

    @Test
    public void shouldNotAcceptNullKey()
    {
        client.setProperty("key: <NULL>", "value: sam", "expectSuccess: false");
    }
}
