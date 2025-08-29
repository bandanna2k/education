package dnt.websockets.integration;

import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.Test;

public class IntegrationWebSocketTest extends AbstractIntegrationVertxTest
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
        client("source1").verifyNoMoreMessages();
        client("source2").verifyNoMoreMessages();

        server.broadcastMessage();

        client("source1").verifyMessage("ServerPushMessage");
        client("source2").verifyMessage("ServerPushMessage");
        client("source1").verifyNoMoreMessages();
        client("source2").verifyNoMoreMessages();
    }

    @Test
    public void shouldFailIfNoResponse()
    {
        client.setProperty("key: do_not_send_response", "value: true", "expectSuccess: false");
    }

    @Test
    public void shouldUseRest()
    {
        rest.getProperty("key: limit", "expectedStatusCode: 404");
        rest.setProperty("key: limit", "value: 1000", "expectedStatusCode: 200");
        rest.getProperty("key: limit", "expectedValue: 1000", "expectedStatusCode: 200");
    }
}
