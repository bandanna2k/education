package dnt.websockets.integration.base;

public interface ToClientTests
{
    void serverShouldRequestAndSucceed();
    void serverShouldRequestAndFail();
    void shouldFailOnNoResponseReceived();

    void serverShouldBroadcast();
    void shouldSupportMultipleClients();
}
