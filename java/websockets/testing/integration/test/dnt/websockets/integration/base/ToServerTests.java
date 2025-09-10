package dnt.websockets.integration.base;

public interface ToServerTests
{
    void clientShouldRequestAndSucceed();
    void clientShouldRequestAndFail();
    void clientShouldPushMessage();
    void shouldFailOnNoResponseReceived();

    void shouldSupportMultipleClients();

    void clientShouldRequestAndSucceed(String source);
    void clientShouldRequestAndFail(String source);
    void clientShouldPushMessage(String source);
    void shouldFailOnNoResponseReceived(String source);

    void shouldNotAcceptEmptyValueWhenSettingProperty(String source);
    void shouldNotAcceptEmptyKeySettingProperty(String source);
    void shouldNotAcceptNullValueWhenSettingProperty(String source);
    void shouldNotAcceptNullKeyWhenSettingProperty(String source);
}
