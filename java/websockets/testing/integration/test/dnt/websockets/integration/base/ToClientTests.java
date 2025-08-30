package dnt.websockets.integration.base;

public interface ToClientTests
{
    void serverShouldRequestAndResponse();
    void serverShouldBroadcast();
    void shouldSupportMultipleClients();
    void shouldFailOnNoResponseReceived();
}
