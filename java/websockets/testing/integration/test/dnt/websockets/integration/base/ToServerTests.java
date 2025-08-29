package dnt.websockets.integration.base;

import org.junit.Test;

public interface ToServerTests
{
    @Test
    void clientShouldRequestAndResponse();

    @Test
    void clientShouldPushMessage();

    @Test
    void serverShouldFutureFailNextMessage();

    @Test
    void serverShouldFailNextMessage();

    @Test
    void shouldFailIfNoResponse();

    @Test
    void shouldSupportMultipleClients();
}
