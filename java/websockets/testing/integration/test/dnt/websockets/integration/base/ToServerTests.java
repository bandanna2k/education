package dnt.websockets.integration.base;

public interface ToServerTests
{
    void clientShouldRequestAndSucceed();
    void clientShouldRequestAndFail();
    void clientShouldPushMessage();
    void shouldFailOnNoResponseReceived();

    void shouldSupportMultipleClients();

    void shouldNotAcceptEmptyValueWhenSettingProperty();
    void shouldNotAcceptEmptyKeySettingProperty();
    void shouldNotAcceptNullValueWhenSettingProperty();
    void shouldNotAcceptNullKeyWhenSettingProperty();
}
