package dnt.websockets.integration.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.communications.*;
import dnt.websockets.integration.MessageCollector;
import dnt.websockets.integration.ServerDriver;
import dnt.websockets.server.ServerMessageProcessor;
import education.common.result.Result;
import io.vertx.core.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ServerDsl
{
    private final ServerDriver serverDriver;
    private final MessageCollector collector;

    public ServerDsl(final ExecutionLayer executionLayer, ServerMessageProcessor serverMessageProcessor, MessageCollector serverMessageCollector)
    {
        this.collector = serverMessageCollector;
        this.serverDriver = new ServerDriver(executionLayer, serverMessageProcessor);
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
        AbstractMessage lastMessage = collector.getLastMessage();
        assertThat(lastMessage).isNotNull();
        assertThat(lastMessage.getClass().getSimpleName()).isEqualTo(className);
    }

    public void getStatusFromClient(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("client"),
                new RequiredArg("expectedStatus"));
        String client = params.value("client");
        String expectedStatus = params.value("expectedStatus");
        Result<GetStatusResponse, String> actual = join(serverDriver.getStatusFromClient(client));
        assertTrue(actual.isSuccess());
        assertThat(actual.success().status).isEqualTo(expectedStatus);
    }

    private static <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }
}
