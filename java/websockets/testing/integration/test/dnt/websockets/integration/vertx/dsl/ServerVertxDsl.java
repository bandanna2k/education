package dnt.websockets.integration.vertx.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.integration.vertx.WebSocketServerDriver;
import dnt.websockets.messages.GetStatusResponse;
import education.common.result.Result;
import io.vertx.core.Future;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerVertxDsl
{
    private final WebSocketServerDriver serverDriver;

    public ServerVertxDsl(WebSocketServerDriver serverDriver)
    {
        this.serverDriver = serverDriver;
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
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
