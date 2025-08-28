package dnt.websockets.server;

import dnt.websockets.communications.*;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;

public class ServerExecutionLayer implements ExecutionLayer
{
    private final Publisher publisher;
    private final VertxAsyncExecutor<AbstractResponse> executor;

    public ServerExecutionLayer(VertxAsyncExecutor<AbstractResponse> executor, Publisher publisher)
    {
        this.executor = executor;
        this.publisher = publisher;
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> serverRequestFromClient(AbstractRequest request)
    {
        return executor.execute(correlationId -> publisher.send(request.attachCorrelationId(correlationId)))
                .map(Result::success)
                .recover(throwable ->
                        Future.succeededFuture(Result.failure(throwable.getMessage())))
                .map(result ->
                        result.map(s -> (T)s, Object::toString));
    }

    @Override
    public void serverResponseToRequest(AbstractResponse response)
    {
        executor.onResponseReceived(response.correlationId, response);
    }

    @Override
    public void serverSend(AbstractMessage message)
    {
        publisher.send(message);
    }

    @Override
    public void clientSend(AbstractMessage message)
    {
    }
}
