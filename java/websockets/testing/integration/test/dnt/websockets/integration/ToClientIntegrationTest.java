package dnt.websockets.integration;

import dnt.websockets.integration.base.AbstractIntegrationTest;
import dnt.websockets.integration.base.ToClientTests;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ToClientIntegrationTest
{
    @Nested
    class MainTests extends AbstractIntegrationTest implements ToClientTests
    {
        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void serverShouldRequestAndSucceed(String source)
        {
            server.getStatusFromClient("client: " + source, "expectedStatus: Wicked");
            client(source).setStatus("Fantastic");
            server.getStatusFromClient("client: " + source, "expectedStatus: Fantastic");

            client(source).verifyMessage("GetStatusRequest");
            server.verifyMessage("GetStatusResponse");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void serverShouldRequestAndFail(String source)
        {
            server.getStatusFromClient("client: " + source, "expectedStatus: Wicked");
            client(source).setStatus("fail_requests");
            server.getStatusFromClient("client: " + source, "expectedErrorMessage: Request not accepted at this time.");
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
        public void shouldSupportMultipleClients()
        {
            client("session1").setStatus("OK");
            client("session2").setStatus("Fine");

            server.getStatusFromClient("client: session1", "expectedStatus: OK");
            server.getStatusFromClient("client: session2", "expectedStatus: Fine");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void shouldFailOnNoResponseReceived(String source)
        {
            server.getStatusFromClient("client: " + source, "expectedStatus: Wicked");
            client(source).setStatus("do_not_send_response");
            server.getStatusFromClient("client: " + source, "expectedErrorMessage: No response received");
        }
    }
}
