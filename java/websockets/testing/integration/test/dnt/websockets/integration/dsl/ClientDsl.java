package dnt.websockets.integration.dsl;

import dnt.websockets.client.ExecutionLayer;
import dnt.websockets.communications.OptionsRequest;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.server.RequestProcessor;
import education.common.result.Result;
import io.vertx.core.Future;
import org.assertj.core.api.Assertions;

public class ClientDsl
{
    private final ClientDriver clientDriver = new ClientDriver();

    public void fetchOptions()
    {
        Result<OptionsResponse, String> result = join(clientDriver.fetchOptions());
        Assertions.assertThat(result.isSuccess()).isTrue();
        System.out.println(result);
    }

    private static <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }
}
