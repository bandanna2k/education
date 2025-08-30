package dnt.websockets.integration.base;

public interface ToServerTests
{
    void clientShouldRequestAndReceive();
    void clientShouldPushMessage();
    void shouldSupportMultipleClients();
    void shouldNotAcceptEmptyValueWhenSettingProperty();
    void shouldNotAcceptEmptyKeySettingProperty();
    void shouldNotAcceptNullValueWhenSettingProperty();
    void shouldNotAcceptNullKeyWhenSettingProperty();
}
