package dnt.websockets.integration.dsl;

import dnt.websockets.client.ExecutionLayer;
import dnt.websockets.communications.OptionsRequest;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.server.RequestProcessor;
import education.common.result.Result;
import io.vertx.core.Future;
import org.assertj.core.api.Assertions;

public class IntegrationDsl
{
    private final ExecutionLayer executionLayer = new IntegrationExecutionLayer(RequestProcessor::new);

    public void fetchOptions()
    {
        Result<OptionsResponse, String> result = join(executionLayer.send(new OptionsRequest()));
        Assertions.assertThat(result.isSuccess()).isTrue();
        System.out.println(result);
    }

    private <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }
}
