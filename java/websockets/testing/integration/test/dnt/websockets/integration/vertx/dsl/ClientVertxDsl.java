package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.communications.OptionsResponse;
import education.common.result.Result;
import io.vertx.core.Future;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientVertxDsl
{
    private final ClientVertxDriver clientDriver = new ClientVertxDriver();

    public void fetchOptions()
    {
        Result<OptionsResponse, String> result = join(clientDriver.requestOptions());
        System.out.println(result);
        assertThat(result.isSuccess()).isTrue();
    }

    private <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }
}
