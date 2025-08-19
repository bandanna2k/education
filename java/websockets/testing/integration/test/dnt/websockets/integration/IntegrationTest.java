package dnt.websockets.integration;

import org.junit.Test;

public class IntegrationTest extends AbstractIntegrationTest
{
    @Test
    public void shouldRequestAndResponse()
    {
        client.fetchOptions();
    }

    @Test
    public void shouldPushMessage()
    {
        server.broadcastMessage();

        client.verifyMessage("PushMessage");
    }
}
