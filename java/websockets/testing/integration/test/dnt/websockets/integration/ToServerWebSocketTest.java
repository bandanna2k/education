package dnt.websockets.integration;

import dnt.websockets.integration.base.ToServerTests;
import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.Ignore;
import org.junit.Test;

public class ToServerWebSocketTest extends AbstractIntegrationVertxTest implements ToServerTests
{
    @Test
    public void clientShouldRequestAndResponse()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectedValue: sam");
    }

    @Ignore
    @Test
    public void clientShouldPushMessage()
    {
    }

    @Test
    public void shouldFailIfNoResponse()
    {
        client.setProperty("key: do_not_send_response", "value: true", "expectSuccess: false");
    }

    @Ignore
    @Test
    public void shouldSupportMultipleClients()
    {
        client("source1").setProperty("key: name", "value: sam", "expectSuccess: true");

        client("source1").verifyMessage("SetPropertyResponse");
        client("source2").verifyNoMoreMessages();
    }

    @Test
    public void shouldNotAcceptEmptyValueWhenSettingProperty()
    {
        client.setProperty("key: name", "value: ",
                "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
    }

    @Test
    public void shouldNotAcceptEmptyKeySettingProperty()
    {
        client.setProperty("key: ", "value: sam",
                "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
    }

    @Test
    public void shouldNotAcceptNullValueWhenSettingProperty()
    {
        client.setProperty("key: name", "value: <NULL>",
                "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
    }

    @Test
    public void shouldNotAcceptNullKeyWhenSettingProperty()
    {
        client.setProperty("key: <NULL>", "value: sam",
                "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
    }
}
