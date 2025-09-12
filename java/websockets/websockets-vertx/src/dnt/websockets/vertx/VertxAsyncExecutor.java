package dnt.websockets.vertx;

import io.vertx.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class VertxAsyncExecutor<Response> implements AsyncExecutor<Response>
{
    private final Vertx vertx;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final Map<Long, AsyncRequestTracking<Response>> asyncPromiseByCorrelationId = new HashMap<>();
    private final long timeoutMillis;

    public VertxAsyncExecutor(final Vertx vertx, final UniqueIdGenerator uniqueIdGenerator)
    {
        this(vertx, uniqueIdGenerator, 5_000);
    }

    VertxAsyncExecutor(final Vertx vertx, final UniqueIdGenerator uniqueIdGenerator, long timeoutMillis)
    {
        this.vertx = vertx;
        this.uniqueIdGenerator = uniqueIdGenerator;
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public Future<Response> execute(final AsyncRequest asyncRequest)
    {
        final long correlationId = uniqueIdGenerator.generateId();
        final Promise<Response> promise = createAndRegisterPromise(correlationId);
        asyncRequest.invoke(correlationId);
        return promise.future();
    }

    private Promise<Response> createAndRegisterPromise(final long correlationId)
    {
        final Promise<Response> asyncPromise = Promise.promise();
        final long timerId = vertx.setTimer(timeoutMillis, id -> timeout(correlationId));
        final AsyncRequestTracking<Response> existingPromise;
        synchronized (asyncPromiseByCorrelationId)
        {
            existingPromise = asyncPromiseByCorrelationId.putIfAbsent(
                    correlationId,
                    new AsyncRequestTracking<>(asyncPromise, vertx.getOrCreateContext(), timerId));
        }
        if (existingPromise != null)
        {
            vertx.cancelTimer(timerId);
            throw new IllegalStateException("Request already inflight for correlation id: " + correlationId);
        }
        return asyncPromise;
    }

    @Override
    public void onResponseReceived(final long correlationId, final Response response)
    {
        final AsyncRequestTracking<Response> asyncRequest;
        synchronized (asyncPromiseByCorrelationId)
        {
            asyncRequest = asyncPromiseByCorrelationId.remove(correlationId);
        }
        if (asyncRequest != null)
        {
            vertx.cancelTimer(asyncRequest.timerId);
            onRequestContext(asyncRequest, v -> asyncRequest.promise.complete(response));
        }
    }

    private void onRequestContext(final AsyncRequestTracking<Response> asyncRequest, final Handler<Void> handler)
    {
        asyncRequest.context.runOnContext(handler);
    }

    private void timeout(final long correlationId)
    {
        final AsyncRequestTracking<Response> asyncRequest;
        synchronized (asyncPromiseByCorrelationId)
        {
            asyncRequest = asyncPromiseByCorrelationId.remove(correlationId);
        }
        if (asyncRequest != null)
        {
            onRequestContext(asyncRequest, v -> asyncRequest.promise.tryFail("Request timed out with correlation id - " + correlationId));
        }
    }

    private static final class AsyncRequestTracking<Response>
    {
        private final Promise<Response> promise;
        private final long timerId;
        private final Context context;

        private AsyncRequestTracking(final Promise<Response> promise, final Context context, final long timerId)
        {
            this.promise = promise;
            this.timerId = timerId;
            this.context = context;
        }
    }

    public static class Builder
    {
        private final Vertx vertx;
        private long initialCorrelationId = System.currentTimeMillis() % 100_000;
        private long timeoutMillis = 5_000;

        public Builder(Vertx vertx)
        {
            this.vertx = vertx;
        }

        public Builder timeoutMillis(long timeoutMillis)
        {
            this.timeoutMillis = timeoutMillis;
            return this;
        }

        public <T> VertxAsyncExecutor<T> build()
        {
            final UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator()
            {
                private final AtomicLong nextCorrelationId = new AtomicLong(initialCorrelationId);

                @Override
                public long generateId()
                {
                    return nextCorrelationId.getAndIncrement();
                }
            };
            return new VertxAsyncExecutor<>(vertx, uniqueIdGenerator, timeoutMillis);
        }
    }
}
