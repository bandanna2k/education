package dnt.websockets.integration;

import dnt.websockets.integration.base.ToServerTests;
import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

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
        @Override @Test
        public void clientShouldRequestAndSucceed()
        {
            client.setProperty("key: name", "value: sam");
            client.getProperty("key: name", "expectedValue: sam");
        }

        @Override @Test
        public void clientShouldRequestAndFail()
        {
            shouldNotAcceptEmptyValueWhenSettingProperty();
            shouldNotAcceptNullValueWhenSettingProperty();
            shouldNotAcceptEmptyKeySettingProperty();
            shouldNotAcceptNullKeyWhenSettingProperty();
        }

        @Override @Test
        public void clientShouldPushMessage()
        {
            server.clearMessages();
            client.pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @Override @Test
        public void shouldFailOnNoResponseReceived()
        {
            client.setProperty("key: do_not_send_response", "value: true", "expectSuccess: false");
        }

        @Override @Test
        public void shouldSupportMultipleClients()
        {
            client("source1").setProperty("key: name", "value: sam", "expectSuccess: true");

            client("source1").verifyMessage("SetPropertyResponse");
            client("source2").verifyNoMoreMessages();
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldNotAcceptEmptyValueWhenSettingProperty()
        {
            client.setProperty("key: name", "value: ",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldNotAcceptEmptyKeySettingProperty()
        {
            client.setProperty("key: ", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldNotAcceptNullValueWhenSettingProperty()
        {
            client.setProperty("key: name", "value: <NULL>",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "source1", "source2" })
        public void shouldNotAcceptNullKeyWhenSettingProperty()
        {
            client.setProperty("key: <NULL>", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }
    }

    private static final List<String> SOURCES = List.of("source1", "source2");

    @Nested
    class ParameterisedTests extends AbstractIntegrationVertxTest implements ToServerTests
    {
        @Override @Test
        public void clientShouldRequestAndSucceed()
        {
            SOURCES.forEach(this::clientShouldRequestAndSucceed);
        }
        private void clientShouldRequestAndSucceed(String source)
        {
            client(source).setProperty("key: name", "value: sam");
            client(source).getProperty("key: name", "expectedValue: sam");
        }

        @Override @Test
        public void clientShouldRequestAndFail()
        {
            SOURCES.forEach(source -> {
                shouldNotAcceptEmptyValueWhenSettingProperty(source);
                shouldNotAcceptNullValueWhenSettingProperty(source);
                shouldNotAcceptEmptyKeySettingProperty(source);
                shouldNotAcceptNullKeyWhenSettingProperty(source);
            });
        }

        @Override @Test
        public void clientShouldPushMessage()
        {
            SOURCES.forEach(this::clientShouldPushMessage);
        }
        private void clientShouldPushMessage(String source)
        {
            client(source).pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @Override @Test
        public void shouldFailOnNoResponseReceived()
        {
            SOURCES.forEach(this::shouldFailOnNoResponseReceived);
        }
        private void shouldFailOnNoResponseReceived(String source)
        {
            client(source).setProperty("key: do_not_send_response", "value: true", "expectSuccess: false");
        }

        @Override @Test
        public void shouldNotAcceptEmptyValueWhenSettingProperty()
        {
            SOURCES.forEach(this::shouldNotAcceptEmptyValueWhenSettingProperty);
        }
        private void shouldNotAcceptEmptyValueWhenSettingProperty(String source)
        {
            client(source).setProperty("key: name", "value: ",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptEmptyKeySettingProperty()
        {
            SOURCES.forEach(this::shouldNotAcceptEmptyKeySettingProperty);
        }
        private void shouldNotAcceptEmptyKeySettingProperty(String source)
        {
            client(source).setProperty("key: ", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptNullValueWhenSettingProperty()
        {
            SOURCES.forEach(this::shouldNotAcceptNullValueWhenSettingProperty);
        }
        private void shouldNotAcceptNullValueWhenSettingProperty(String source)
        {
            client(source).setProperty("key: name", "value: <NULL>",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptNullKeyWhenSettingProperty()
        {
            SOURCES.forEach(this::shouldNotAcceptNullKeyWhenSettingProperty);
        }
        private void shouldNotAcceptNullKeyWhenSettingProperty(String source)
        {
            client(source).setProperty("key: <NULL>", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }

        @Override public void shouldSupportMultipleClients() {}
    }
}
