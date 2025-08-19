package dnt.websockets.integration;

import dnt.websockets.client.ExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.RequestProcessor;
import education.common.result.Result;
import io.vertx.core.Future;

public class IntegrationExecutionLayer implements ExecutionLayer
{
    private final RequestProcessor requestProcessor;
    private final IntegrationPublisher publisher;

    public IntegrationExecutionLayer(RequestProcessorFactory requestProcessorFactory)
    {
        this.publisher = new IntegrationPublisher();
        this.requestProcessor = requestProcessorFactory.createInstance(publisher);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> send(AbstractRequest request)
    {
        return Future.succeededFuture()
                .map(unused ->
                {
                    sendAndProcessImmediately(request);
                    return Result.success((T) publisher.getLastMessage());
                })
                .map(r -> r.mapError(String::valueOf));
    }

    private void sendAndProcessImmediately(AbstractMessage message)
    {
        if (message instanceof OptionsRequest)
        {
            requestProcessor.visit((OptionsRequest) message);
        }
    }

    public interface RequestProcessorFactory
    {
        RequestProcessor createInstance(Publisher publisher);
    }
}
