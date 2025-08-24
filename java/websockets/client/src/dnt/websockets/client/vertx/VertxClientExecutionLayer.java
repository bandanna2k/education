package dnt.websockets.client.vertx;

import dnt.websockets.communications.*;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.concurrent.atomic.AtomicLong;

public class VertxClientExecutionLayer implements ExecutionLayer
{
    private final Publisher publisher;
    private final VertxAsyncExecutor<AbstractResponse> executor;

    public VertxClientExecutionLayer(Vertx vertx, Publisher publisher)
    {
        this.publisher = publisher;
        VertxAsyncExecutor.UniqueIdGenerator uniqueIdGenerator = new VertxAsyncExecutor.UniqueIdGenerator()
        {
            private final AtomicLong nextCorrelationId = new AtomicLong(1);

            @Override
            public long generateId()
            {
                return nextCorrelationId.getAndIncrement();
            }
        };
        this.executor = new VertxAsyncExecutor<>(vertx, uniqueIdGenerator);
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
