package dnt.websockets.integration;

import dnt.websockets.integration.dsl.AbstractIntegrationTest;
import org.junit.Test;

public class ToClientTests extends AbstractIntegrationTest
{
    @Test
    public void serverShouldRequestAndResponse()
    {
        server.getStatusFromClient("client: session1", "expectedStatus: Wicked");
        client.setStatus("Fantastic");
        server.getStatusFromClient("client: session1", "expectedStatus: Fantastic");
    }

    @Test
    public void serverShouldPushMessage()
    {
        client("session1").verifyNoMoreMessages();
        client("session2").verifyNoMoreMessages();

        server.broadcastMessage();

        client("session1").verifyMessage("ServerPushMessage");
        client("session2").verifyMessage("ServerPushMessage");
    }

    @Test
    public void shouldReportNoResponseReceived()
    {
        server.getStatusFromClient("client: session1", "expectedStatus: Wicked");
        client.setStatus("do_not_send_response");
        server.getStatusFromClient("client: session1", "expectedErrorMessage: No response received");
    }

    @Test
    public void shouldSupportMultipleClients()
    {
        client("session1").setStatus("OK");
        client("session2").setStatus("Fine");

        server.getStatusFromClient("client: session1", "expectedStatus: OK");
        server.getStatusFromClient("client: session2", "expectedStatus: Fine");
    }

}
