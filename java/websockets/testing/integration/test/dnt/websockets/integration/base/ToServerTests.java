package dnt.websockets.integration.base;

public interface ToServerTests
{
    void clientShouldRequestAndSucceed();
    void clientShouldRequestAndFail();
    void clientShouldPushMessage();
    void shouldSupportMultipleClients();
    void shouldFailOnNoResponseReceived();

    void shouldNotAcceptEmptyValueWhenSettingProperty();
    void shouldNotAcceptEmptyKeySettingProperty();
    void shouldNotAcceptNullValueWhenSettingProperty();
    void shouldNotAcceptNullKeyWhenSettingProperty();
}
