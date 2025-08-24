package dnt.websockets.integration.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.PushMessage;
import dnt.websockets.integration.ServerDriver;
import dnt.websockets.server.RequestProcessor;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerDsl
{
    private final ServerDriver serverDriver;

    public ServerDsl(final ExecutionLayer executionLayer, RequestProcessor requestProcessor)
    {
        serverDriver = new ServerDriver(executionLayer, requestProcessor);
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage(new PushMessage());
    }

    public void verifyProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new RequiredArg("expectedValue"));
        String key = params.value("key");
        String expectedValue = params.value("expectedValue");
        String actual = serverDriver.getProperty(key);
        assertThat(actual).isEqualTo(expectedValue);
    }
}
