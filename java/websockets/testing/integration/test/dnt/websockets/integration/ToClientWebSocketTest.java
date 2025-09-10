package dnt.websockets.integration;

import dnt.websockets.integration.base.ToClientTests;
import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ToClientWebSocketTest
{
    @Nested
    class MainTests extends AbstractIntegrationVertxTest implements ToClientTests
    {
        @Test
        public void serverShouldRequestAndSucceed()
        {
            server.getStatusFromClient("client: source1", "expectedStatus: Wicked");
            client.setStatus("Fantastic");
            server.getStatusFromClient("client: source1", "expectedStatus: Fantastic");
        }

        @Test
        public void serverShouldRequestAndFail()
        {
            server.getStatusFromClient("client: source1", "expectedStatus: Wicked");
            client.setStatus("fail_requests");
            server.getStatusFromClient("client: source1", "expectedErrorMessage: Request not accepted at this time.");
        }

        @Test
        public void shouldFailOnNoResponseReceived()
        {
            server.getStatusFromClient("client: source1", "expectedStatus: Wicked");
            client.setStatus("do_not_send_response");
            server.getStatusFromClient("client: source1", "expectedErrorMessage: Request timed out");
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
        public void shouldSupportMultipleClients()
        {
            client("source1").setStatus("OK");
            client("source2").setStatus("Fine");

            server.getStatusFromClient("client: source1", "expectedStatus: OK");
            server.getStatusFromClient("client: source2", "expectedStatus: Fine");

            server.verifyMessage("GetStatusResponse");
            server.verifyMessage("GetStatusResponse");
            server.verifyNoMoreMessages();
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void serverShouldRequestAndSucceed(String source)
        {
            server.getStatusFromClient("client: " + source, "expectedStatus: Wicked");
            client(source).setStatus("Fantastic");
            server.getStatusFromClient("client: " + source, "expectedStatus: Fantastic");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void serverShouldRequestAndFail(String source)
        {
            server.getStatusFromClient("client: " + source, "expectedStatus: Wicked");
            client(source).setStatus("fail_requests");
            server.getStatusFromClient("client: " + source, "expectedErrorMessage: Request not accepted at this time.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldFailOnNoResponseReceived(String source)
        {
            server.getStatusFromClient("client: " + source, "expectedStatus: Wicked");
            client(source).setStatus("do_not_send_response");
            server.getStatusFromClient("client: " + source, "expectedErrorMessage: Request timed out");
        }
    }
}
