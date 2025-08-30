package dnt.websockets.integration.base;

public interface ToClientTests
{
    void serverShouldRequestAndSucceed();
    void serverShouldRequestAndFail();
    void serverShouldBroadcast();
    void shouldSupportMultipleClients();
    void shouldFailOnNoResponseReceived();
}
