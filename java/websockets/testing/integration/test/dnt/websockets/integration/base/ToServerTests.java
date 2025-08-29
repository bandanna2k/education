package dnt.websockets.integration.base;

public interface ToServerTests
{
    void clientShouldRequestAndResponse();
    void clientShouldPushMessage();
    void shouldSupportMultipleClients();
    void shouldNotAcceptEmptyValueWhenSettingProperty();
    void shouldNotAcceptEmptyKeySettingProperty();
    void shouldNotAcceptNullValueWhenSettingProperty();
    void shouldNotAcceptNullKeyWhenSettingProperty();
}
