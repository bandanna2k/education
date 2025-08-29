package dnt.websockets.integration;

import dnt.websockets.integration.dsl.AbstractIntegrationTest;
import org.junit.Test;

public class ToClientTests extends AbstractIntegrationTest
{
    @Test
    public void serverShouldRequestAndResponse()
    {
        server.getStatusFromClient("client: client", "expectedStatus: Wicked");
    }

    @Test
    public void serverShouldPushMessage()
    {
        client.verifyNoMoreMessages();
        server.broadcastMessage();
        client.verifyMessage("ServerPushMessage");
    }

}
