package dnt.websockets.integration.vertx;

import dnt.websockets.integration.AbstractIntegrationTest;
import org.junit.Test;

public class IntegrationVertxTest extends AbstractIntegrationVertxTest
{
    @Test
    public void shouldSendAndReceive()
    {
        client.fetchOptions();
        client.fetchOptions();
    }
}
