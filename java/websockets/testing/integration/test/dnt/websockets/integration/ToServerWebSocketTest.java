package dnt.websockets.integration;

import dnt.websockets.integration.base.ToServerTests;
import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ToServerWebSocketTest
{
    @Nested
    class OtherTests extends AbstractIntegrationVertxTest
    {
        @Test
        public void shouldUseRest()
        {
            rest.getProperty("key: limit", "expectedStatusCode: 404");
            rest.setProperty("key: limit", "value: 1000", "expectedStatusCode: 200");
            rest.getProperty("key: limit", "expectedValue: 1000", "expectedStatusCode: 200");
        }
    }

    @Nested
    class MainTests extends AbstractIntegrationVertxTest implements ToServerTests
    {
        @Test
        public void clientShouldRequestAndSucceed()
        {
            client.setProperty("key: name", "value: sam");
            client.getProperty("key: name", "expectedValue: sam");
        }

        @Test
        public void clientShouldRequestAndFail()
        {
            shouldNotAcceptEmptyValueWhenSettingProperty("source1");
            shouldNotAcceptNullValueWhenSettingProperty("source1");
            shouldNotAcceptEmptyKeySettingProperty("source1");
            shouldNotAcceptNullKeyWhenSettingProperty("source1");
        }

        @Test
        public void clientShouldPushMessage()
        {
            client.pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @Test
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

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void clientShouldRequestAndSucceed(String source)
        {
            client(source).setProperty("key: name", "value: sam");
            client(source).getProperty("key: name", "expectedValue: sam");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void clientShouldRequestAndFail(String source)
        {
            shouldNotAcceptEmptyValueWhenSettingProperty(source);
            shouldNotAcceptNullValueWhenSettingProperty(source);
            shouldNotAcceptEmptyKeySettingProperty(source);
            shouldNotAcceptNullKeyWhenSettingProperty(source);
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void clientShouldPushMessage(String source)
        {
            client(source).pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldFailOnNoResponseReceived(String source)
        {
            client(source).setProperty("key: do_not_send_response", "value: true", "expectSuccess: false");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldNotAcceptEmptyValueWhenSettingProperty(String source)
        {
            client(source).setProperty("key: name", "value: ",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldNotAcceptEmptyKeySettingProperty(String source)
        {
            client(source).setProperty("key: ", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldNotAcceptNullValueWhenSettingProperty(String source)
        {
            client(source).setProperty("key: name", "value: <NULL>",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldNotAcceptNullKeyWhenSettingProperty(String source)
        {
            client(source).setProperty("key: <NULL>", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }
    }
}
