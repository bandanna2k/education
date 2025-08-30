package dnt.websockets.integration;

import dnt.websockets.integration.base.ToClientTests;
import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.Ignore;
import org.junit.Test;

public class ToClientWebSocketTest extends AbstractIntegrationVertxTest implements ToClientTests
{
    @Override
    @Test
    public void serverShouldRequestAndResponse()
    {
        server.getStatusFromClient("client: source1", "expectedStatus: Wicked");
        client.setStatus("Fantastic");
        server.getStatusFromClient("client: source1", "expectedStatus: Fantastic");
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
    public void shouldReportNoResponseReceived()
    {
        server.getStatusFromClient("client: source1", "expectedStatus: Wicked");
        client.setStatus("do_not_send_response");
        server.getStatusFromClient("client: source1", "expectedErrorMessage: Request timed out");
    }

    @Override
    @Test
    @Ignore
    public void shouldSupportMultipleClients()
    {

    }
}
