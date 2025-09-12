package dnt.websockets.integration;

import dnt.websockets.integration.base.AbstractIntegrationTest;
import dnt.websockets.integration.base.ToClientTests;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ToClientIntegrationTest
{
    private static final List<String> SESSIONS = List.of("session1", "session2");

    @Nested
    class MainTests extends AbstractIntegrationTest implements ToClientTests
    {
        @Override @Test
        public void serverShouldRequestAndSucceed()
        {
            server.getStatusFromClient("client: session1", "expectedStatus: Wicked");
            client.setStatus("Fantastic");
            server.getStatusFromClient("client: session1", "expectedStatus: Fantastic");

            client.verifyMessage("GetStatusRequest");
            server.verifyMessage("GetStatusResponse");
        }

        @Override @Test
        public void serverShouldRequestAndFail()
        {
            server.getStatusFromClient("client: session1", "expectedStatus: Wicked");
            client.setStatus("fail_requests");
            server.getStatusFromClient("client: session1", "expectedErrorMessage: Request not accepted at this time.");
        }

        @Override @Test
        public void serverShouldBroadcast()
        {
            client("session1").verifyNoMoreMessages();
            client("session2").verifyNoMoreMessages();

            server.broadcastMessage();

            client("session1").verifyMessage("ServerPushMessage");
            client("session2").verifyMessage("ServerPushMessage");
        }

        @Override @Test
        public void shouldFailOnNoResponseReceived()
        {
            server.getStatusFromClient("client: session1", "expectedStatus: Wicked");
            client.setStatus("do_not_send_response");
            server.getStatusFromClient("client: session1", "expectedErrorMessage: No response received");
        }

        @Override @Test
        public void shouldSupportMultipleClients()
        {
            client("session1").setStatus("OK");
            client("session2").setStatus("Fine");

            server.getStatusFromClient("client: session1", "expectedStatus: OK");
            server.getStatusFromClient("client: session2", "expectedStatus: Fine");
        }
    }

    @Nested
    class MultiSessionTests extends AbstractIntegrationTest implements ToClientTests
    {
        @Override @Test
        public void serverShouldRequestAndSucceed()
        {
            SESSIONS.forEach(this::serverShouldRequestAndSucceed);
        }
        public void serverShouldRequestAndSucceed(String session)
        {
            server.getStatusFromClient("client: " + session, "expectedStatus: Wicked");
            client(session).setStatus("Fantastic");
            server.getStatusFromClient("client: " + session, "expectedStatus: Fantastic");

            client(session).verifyMessage("GetStatusRequest");
            server.verifyMessage("GetStatusResponse");
        }

        @Override @Test
        public void serverShouldRequestAndFail()
        {
            SESSIONS.forEach(this::serverShouldRequestAndFail);
        }
        public void serverShouldRequestAndFail(String session)
        {
            server.getStatusFromClient("client: " + session, "expectedStatus: Wicked");
            client(session).setStatus("fail_requests");
            server.getStatusFromClient("client: " + session, "expectedErrorMessage: Request not accepted at this time.");
        }

        @Override @Test
        public void shouldFailOnNoResponseReceived()
        {
            SESSIONS.forEach(this::shouldFailOnNoResponseReceived);
        }
        public void shouldFailOnNoResponseReceived(String session)
        {
            server.getStatusFromClient("client: " + session, "expectedStatus: Wicked");
            client(session).setStatus("do_not_send_response");
            server.getStatusFromClient("client: " + session, "expectedErrorMessage: No response received");
        }

        @Override public void serverShouldBroadcast() {}
        @Override public void shouldSupportMultipleClients() {}
    }
}
