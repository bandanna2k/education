package dnt.websockets.integration;

import dnt.websockets.integration.base.ToServerTests;
import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class ToServerWebSocketTest
{
    public static class OtherTests extends AbstractIntegrationVertxTest
    {
        @Test
        public void shouldUseRest()
        {
            rest.getProperty("key: limit", "expectedStatusCode: 404");
            rest.setProperty("key: limit", "value: 1000", "expectedStatusCode: 200");
            rest.getProperty("key: limit", "expectedValue: 1000", "expectedStatusCode: 200");
        }
    }

    public static class MainTests extends AbstractIntegrationVertxTest implements ToServerTests
    {
        @Test
        public void clientShouldRequestAndSucceed()
        {
            client.setProperty("key: name", "value: sam");
            client.getProperty("key: name", "expectedValue: sam");
        }

        @Override
        public void clientShouldRequestAndFail()
        {
            shouldNotAcceptEmptyValueWhenSettingProperty();
            shouldNotAcceptNullValueWhenSettingProperty();
            shouldNotAcceptEmptyKeySettingProperty();
            shouldNotAcceptNullKeyWhenSettingProperty();
        }

        @Test
        public void clientShouldPushMessage()
        {
            client.pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @Override
        public void shouldFailOnNoResponseReceived()
        {
            client.setProperty("key: do_not_send_response", "value: true", "expectSuccess: false");
        }

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
}
