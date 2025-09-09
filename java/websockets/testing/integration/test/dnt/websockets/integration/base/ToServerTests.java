package dnt.websockets.integration.base;

public interface ToServerTests
{
    void clientShouldRequestAndSucceed(String source);
    void clientShouldRequestAndFail(String source);
    void clientShouldPushMessage(String source);
    void shouldSupportMultipleClients();
    void shouldFailOnNoResponseReceived(String source);

    void shouldNotAcceptEmptyValueWhenSettingProperty(String source);
    void shouldNotAcceptEmptyKeySettingProperty(String source);
    void shouldNotAcceptNullValueWhenSettingProperty(String source);
    void shouldNotAcceptNullKeyWhenSettingProperty(String source);
}
