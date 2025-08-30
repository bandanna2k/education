package dnt.websockets.integration;

import dnt.websockets.integration.base.AbstractIntegrationTest;
import dnt.websockets.integration.base.ToClientTests;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class ToClientIntegrationTest
{
    public static class MainTests extends AbstractIntegrationTest implements ToClientTests
    {
        @Test
        public void serverShouldRequestAndResponse()
        {
            server.getStatusFromClient("client: session1", "expectedStatus: Wicked");
            client.setStatus("Fantastic");
            server.getStatusFromClient("client: session1", "expectedStatus: Fantastic");
        }

        @Test
        public void serverShouldBroadcast()
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
}
