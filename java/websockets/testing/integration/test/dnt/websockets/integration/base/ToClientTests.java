package dnt.websockets.integration.base;

public interface ToClientTests
{
    void serverShouldRequestAndSucceed(String source);
    void serverShouldRequestAndFail(String source);
    void shouldFailOnNoResponseReceived(String source);
    void serverShouldBroadcast();
    void shouldSupportMultipleClients();
}
