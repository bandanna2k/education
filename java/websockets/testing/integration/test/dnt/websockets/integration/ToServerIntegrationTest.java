package dnt.websockets.integration;

import dnt.websockets.integration.base.AbstractIntegrationTest;
import dnt.websockets.integration.base.ToServerTests;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ToServerIntegrationTest
{
    private static final List<String> SESSIONS = List.of("session2", "session1");

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
        @Override @Test
        public void clientShouldRequestAndSucceed()
        {
            client.setProperty("key: name", "value: sam");
            client.getProperty("key: name", "expectedValue: sam");

            server.verifyMessage("SetPropertyRequest");
            client.verifyMessage("SetPropertyResponse");
        }

        @Override @Test
        public void clientShouldRequestAndFail()
        {
            shouldNotAcceptEmptyKeySettingProperty();
            shouldNotAcceptEmptyValueWhenSettingProperty();
            shouldNotAcceptNullKeyWhenSettingProperty();
            shouldNotAcceptNullValueWhenSettingProperty();
        }

        @Override @Test
        public void clientShouldPushMessage()
        {
            client.pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @Override @Test
        public void shouldFailOnNoResponseReceived()
        {
            client.setProperty("key: do_not_send_response", "value: true",
                    "expectSuccess: false", "expectedErrorMessage: No response received");
        }

        @Override @Test
        public void shouldSupportMultipleClients()
        {
            client("session1").setProperty("key: name", "value: sam", "expectSuccess: true");
            client("session2").getProperty("key: name", "expectedValue: sam");
        }

        @Override @Test
        public void shouldNotAcceptEmptyValueWhenSettingProperty()
        {
            client.setProperty("key: name", "value: ",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptEmptyKeySettingProperty()
        {
            client.setProperty("key: ", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptNullValueWhenSettingProperty()
        {
            client.setProperty("key: name", "value: <NULL>",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptNullKeyWhenSettingProperty()
        {
            client.setProperty("key: <NULL>", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }
    }

    @Nested
    public class MultiSessionTests extends AbstractIntegrationTest implements ToServerTests
    {
        @Override @Test
        public void clientShouldRequestAndSucceed()
        {
            SESSIONS.forEach(this::clientShouldRequestAndSucceed);
        }
        private void clientShouldRequestAndSucceed(String session)
        {
            server.clearMessages();
            client(session).setProperty("key: name", "value: sam");
            client(session).getProperty("key: name", "expectedValue: sam");

            server.verifyMessage("SetPropertyRequest");
            client(session).verifyMessage("SetPropertyResponse");
        }

        @Override @Test
        public void clientShouldRequestAndFail()
        {
            SESSIONS.forEach(source -> {
                shouldNotAcceptEmptyKeySettingProperty(source);
                shouldNotAcceptEmptyValueWhenSettingProperty(source);
                shouldNotAcceptNullKeyWhenSettingProperty(source);
                shouldNotAcceptNullValueWhenSettingProperty(source);
            });
        }

        @Override @Test
        public void clientShouldPushMessage()
        {
            SESSIONS.forEach(this::clientShouldPushMessage);
        }
        private void clientShouldPushMessage(String session)
        {
            client(session).pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @Override @Test
        public void shouldFailOnNoResponseReceived()
        {
            SESSIONS.forEach(this::shouldFailOnNoResponseReceived);
        }
        private void shouldFailOnNoResponseReceived(String session)
        {
            client(session).setProperty("key: do_not_send_response", "value: true",
                    "expectSuccess: false", "expectedErrorMessage: No response received");
        }

        @Override
        public void shouldSupportMultipleClients() {}

        @Override @Test
        public void shouldNotAcceptEmptyValueWhenSettingProperty()
        {
            SESSIONS.forEach(this::shouldNotAcceptEmptyValueWhenSettingProperty);
        }
        private void shouldNotAcceptEmptyValueWhenSettingProperty(String session)
        {
            client(session).setProperty("key: name", "value: ",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptEmptyKeySettingProperty()
        {
            SESSIONS.forEach(this::shouldNotAcceptEmptyKeySettingProperty);
        }
        private void shouldNotAcceptEmptyKeySettingProperty(String session)
        {
            client(session).setProperty("key: ", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptNullValueWhenSettingProperty()
        {
            SESSIONS.forEach(this::shouldNotAcceptNullValueWhenSettingProperty);
        }
        private void shouldNotAcceptNullValueWhenSettingProperty(String session)
        {
            client(session).setProperty("key: name", "value: <NULL>",
                    "expectSuccess: false", "expectedErrorMessage: Value cannot be empty.");
        }

        @Override @Test
        public void shouldNotAcceptNullKeyWhenSettingProperty()
        {
            SESSIONS.forEach(this::shouldNotAcceptNullKeyWhenSettingProperty);
        }
        private void shouldNotAcceptNullKeyWhenSettingProperty(String session)
        {
            client(session).setProperty("key: <NULL>", "value: sam",
                    "expectSuccess: false", "expectedErrorMessage: Key cannot be empty.");
        }
    }
}
