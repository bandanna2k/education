package dnt.websockets.integration.vertx;

import org.junit.Test;

public class IntegrationVertxTest extends AbstractIntegrationVertxTest
{
    @Test
    public void shouldSendAndReceive()
    {
        client.fetchOptions();
        client.fetchOptions();
    }

    @Test
    public void shouldPushMessage()
    {
        server.broadcastMessage();

        client.verifyMessage("PushMessage");
    }
}
