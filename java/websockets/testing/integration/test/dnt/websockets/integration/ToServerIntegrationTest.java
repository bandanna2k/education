package dnt.websockets.integration;

import dnt.websockets.integration.base.AbstractIntegrationTest;
import dnt.websockets.integration.base.ToClientTests;
import dnt.websockets.integration.base.ToServerTests;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class ToServerIntegrationTest
{
    public static class OtherTests extends AbstractIntegrationTest
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
    public static class MainTests extends AbstractIntegrationTest implements ToServerTests
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
            shouldNotAcceptEmptyKeySettingProperty();
            shouldNotAcceptEmptyValueWhenSettingProperty();
            shouldNotAcceptNullKeyWhenSettingProperty();
            shouldNotAcceptNullValueWhenSettingProperty();
        }

        @Test
        public void clientShouldPushMessage()
        {
            client.pushPulse("rate: 60", "sequence: 1");
            server.verifyMessage("ClientPushPulse");
        }

        @Test
        public void shouldSupportMultipleClients()
        {
            client("session1").setProperty("key: name", "value: sam", "expectSuccess: true");

            client("session1").verifyMessage("SetPropertyResponse");
            client("session2").verifyNoMoreMessages();
        }

        @Override
        public void shouldFailOnNoResponseReceived()
        {
            client.setProperty("key: do_not_send_response", "value: true",
                    "expectSuccess: false", "expectedErrorMessage: No response received");
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
