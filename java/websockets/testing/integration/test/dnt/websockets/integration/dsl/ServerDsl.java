package dnt.websockets.integration.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.integration.MessageCollector;
import dnt.websockets.integration.ServerDriver;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.GetStatusResponse;
import dnt.websockets.messages.ServerPushMessage;
import education.common.result.Result;
import io.vertx.core.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ServerDsl
{
    private final ServerDriver serverDriver;
    private final MessageCollector collector;

    public ServerDsl(ServerDriver serverDriver, MessageCollector serverMessageCollector)
    {
        this.collector = serverMessageCollector;
        this.serverDriver = serverDriver;
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
                new OptionalArg("expectedStatus"),
                new OptionalArg("expectedErrorMessage"));
        String client = params.value("client");
        String expectedStatus = params.value("expectedStatus");
        Result<GetStatusResponse, String> actual = join(serverDriver.getStatusFromClient(client));
        actual.consume(
                response -> assertThat(response.status).isEqualTo(expectedStatus),
                error -> assertThat(error).isEqualTo(params.value("expectedErrorMessage")));
    }

    private static <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }
}
