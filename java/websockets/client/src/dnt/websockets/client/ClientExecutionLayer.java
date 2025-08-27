package dnt.websockets.client;

import dnt.websockets.communications.*;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.concurrent.atomic.AtomicLong;

public class ClientExecutionLayer implements ExecutionLayer
{
    private final Publisher publisher;
    private final VertxAsyncExecutor<AbstractResponse> executor;

    public ClientExecutionLayer(VertxAsyncExecutor<AbstractResponse> executor, Publisher publisher)
    {
        this.executor = executor;
        this.publisher = publisher;
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request)
    {
        return executor.execute(correlationId -> publisher.send(request.attachCorrelationId(correlationId)))
                .map(Result::success)
                .recover(throwable ->
                        Future.succeededFuture(Result.failure(throwable.getMessage())))
                .map(result ->
                        result.map(s -> (T)s, Object::toString));
    }

    @Override
    public void respond(AbstractResponse response)
    {
        executor.onResponseReceived(response.correlationId, response);
    }

    @Override
    public void send(AbstractMessage message)
    {
        throw new UnsupportedOperationException("Client doesn't send unsolicited messages to server.");
    }
}
