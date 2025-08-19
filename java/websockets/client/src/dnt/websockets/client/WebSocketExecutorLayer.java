package dnt.websockets.client;

import dnt.websockets.communications.*;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.concurrent.atomic.AtomicLong;

public class WebSocketExecutorLayer
{
    private final MessagePublisher messagePublisher;
    private final VertxAsyncExecutor<AbstractResponse> executor;

    public WebSocketExecutorLayer(Vertx vertx, MessagePublisher messagePublisher)
    {
        this.messagePublisher = messagePublisher;
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

    public <T extends AbstractResponse> Future<Result<T, String>> send(AbstractRequest request)
    {
        return executor.execute(correlationId -> messagePublisher.send(request.attachCorrelationId(correlationId)))
                .map(Result::success)
                .recover(throwable ->
                        Future.succeededFuture(Result.failure(throwable.getMessage())))
                .map(result ->
                        result.map(s -> (T)s, Object::toString));
    }

    public void onResponseReceived(long correlationId, OptionsResponse optionsResponse)
    {
        executor.onResponseReceived(correlationId, optionsResponse);
    }
}
