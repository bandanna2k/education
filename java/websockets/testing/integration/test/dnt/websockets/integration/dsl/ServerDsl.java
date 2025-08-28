package dnt.websockets.integration.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.ServerPushMessage;
import dnt.websockets.integration.ServerDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerDsl
{
    private final ServerDriver serverDriver;
    private final TestServerMessageCollector messageProcessor;

    public ServerDsl(final ExecutionLayer executionLayer, TestServerMessageCollector messageProcessor)
    {
        this.messageProcessor = messageProcessor;
        this.serverDriver = new ServerDriver(executionLayer, messageProcessor);
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage(new ServerPushMessage());
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

    public void verifyMessage(String className)
    {
        AbstractMessage lastMessage = messageProcessor.getLastMessage();
        assertThat(lastMessage).isNotNull();
        assertThat(lastMessage.getClass().getSimpleName()).isEqualTo(className);
    }
}
