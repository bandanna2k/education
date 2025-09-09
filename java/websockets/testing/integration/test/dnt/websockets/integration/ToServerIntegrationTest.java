package dnt.websockets.integration;

import dnt.websockets.integration.base.AbstractIntegrationTest;
import dnt.websockets.integration.base.ToServerTests;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ToServerIntegrationTest
{
    @Nested
    public class OtherTests extends AbstractIntegrationTest
    {
        @Test
        public void serverShouldFutureFailNextMessage()
        {
            client.setProperty("key: name", "value: sam");
            client.getProperty("key: name", "expectException: false");

            integration.throwOnNextMessage();

            client.getProperty("key: name", "expectException: true");
        }

        @Test
        public void serverShouldFailNextMessage()
        {
            client.setProperty("key: name", "value: sam", "expectSuccess: true");

            integration.failNextMessage("Not available for this user.");

            client.setProperty("key: name", "value: sam", "expectSuccess: false");
        }

        @Test
        public void shouldPauseProcessing()
        {
            client.setProperty("key: name", "value: alex", "complete: true");

            integration.pauseProcessing();
            client.setProperty("key: name", "value: drew", "complete: false");
            client.setProperty("key: name", "value: riley", "complete: false");
            client.setProperty("key: name", "value: sam", "complete: false");
            client.setProperty("key: name", "value: terry", "complete: false");

            server.verifyProperty("key: name", "expectedValue: alex");

            integration.resumeProcessing("messageCount: 1");
            server.verifyProperty("key: name", "expectedValue: drew");

            integration.resumeProcessing();
            server.verifyProperty("key: name", "expectedValue: terry");
        }
    }

    @Nested
    public class MainTests extends AbstractIntegrationTest implements ToServerTests
    {
        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void clientShouldRequestAndSucceed(String source)
        {
            client(source).setProperty("key: name", "value: sam");
            client(source).getProperty("key: name", "expectedValue: sam");

            server.verifyMessage("SetPropertyRequest");
            client(source).verifyMessage("SetPropertyResponse");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void clientShouldRequestAndFail(String source)
        {
            shouldNotAcceptEmptyKeySettingProperty(source);
            shouldNotAcceptEmptyValueWhenSettingProperty(source);
            shouldNotAcceptNullKeyWhenSettingProperty(source);
            shouldNotAcceptNullValueWhenSettingProperty(source);
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void clientShouldPushMessage(String source)
        {
            client(source).pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @Test
        public void shouldSupportMultipleClients()
        {
            client("session1").clearMessages();
            client("session2").clearMessages();

            client("session1").setProperty("key: name", "value: sam", "expectSuccess: true");

            client("session1").verifyMessage("SetPropertyResponse");
            client("session2").verifyNoMoreMessages();
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void shouldFailOnNoResponseReceived(String source)
        {
            client(source).setProperty("key: do_not_send_response", "value: true",
                    "expectSuccess: false", "expectedErrorMessage: No response received");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void shouldNotAcceptEmptyValueWhenSettingProperty(String source)
        {
            client(source).setProperty("key: name", "value: ",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void shouldNotAcceptEmptyKeySettingProperty(String source)
        {
            client(source).setProperty("key: ", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void shouldNotAcceptNullValueWhenSettingProperty(String source)
        {
            client(source).setProperty("key: name", "value: <NULL>",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @ParameterizedTest(name = "Test {index}: source={0}")
        @ValueSource(strings = { "session1", "session2" })
        public void shouldNotAcceptNullKeyWhenSettingProperty(String source)
        {
            client.setProperty("key: <NULL>", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }
    }
}
