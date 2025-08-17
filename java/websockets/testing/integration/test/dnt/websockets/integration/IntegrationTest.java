package dnt.websockets.integration;

import org.junit.Test;

public class IntegrationTest extends AbstractIntegrationTest
{
    @Test
    public void shouldSendAndReceive()
    {
        client.fetchOptions();
        client.verifyOptions();
    }
}
