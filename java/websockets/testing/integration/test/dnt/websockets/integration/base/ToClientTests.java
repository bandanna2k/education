package dnt.websockets.integration.base;

import org.junit.Test;

public interface ToClientTests
{
    @Test
    void serverShouldRequestAndResponse();

    @Test
    void serverShouldBroadcast();

    @Test
    void shouldReportNoResponseReceived();

    @Test
    void shouldSupportMultipleClients();
}
