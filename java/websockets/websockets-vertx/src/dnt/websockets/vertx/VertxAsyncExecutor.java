package dnt.websockets.vertx;

import io.vertx.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VertxAsyncExecutor<Response>
{
    private final Vertx vertx;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final Map<Long, AsyncRequestTracking<Response>> asyncPromiseByCorrelationId = new HashMap<>();
    private volatile Throwable timeoutResponse;
    private volatile String timeoutMessage;

    public VertxAsyncExecutor(final Vertx vertx, final UniqueIdGenerator uniqueIdGenerator)
    {
        this.vertx = vertx;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    public VertxAsyncExecutor<Response> onTimeoutReturn(final Throwable timeoutResponse)
    {
        assert timeoutMessage == null;
        this.timeoutResponse = timeoutResponse;
        return this;
    }

    public VertxAsyncExecutor<Response> onTimeoutReturn(final String timeoutMessage)
    {
        assert timeoutResponse == null;
        this.timeoutMessage = timeoutMessage;
        return this;
    }

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
        final long timerId = vertx.setTimer(5_000, id -> timeout(correlationId));
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

    public void onError(final long correlationId, final String message)
    {
        final AsyncRequestTracking<Response> asyncRequest;
        synchronized (asyncPromiseByCorrelationId)
        {
            asyncRequest = asyncPromiseByCorrelationId.remove(correlationId);
        }
        if (asyncRequest != null)
        {
            vertx.cancelTimer(asyncRequest.timerId);
            onRequestContext(asyncRequest, v -> asyncRequest.promise.fail(message));
        }
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
            if (timeoutResponse != null)
            {
                onRequestContext(asyncRequest, v -> asyncRequest.promise.tryFail(timeoutResponse));
            }
            else
            {
                onRequestContext(asyncRequest, v -> asyncRequest.promise.tryFail(Objects.requireNonNullElse(timeoutMessage, "Request timed out with correlation id - " + correlationId)));
            }
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

    public interface UniqueIdGenerator
    {
        long generateId();
    }

    @FunctionalInterface
    public interface AsyncRequest
    {
        void invoke(long correlationId);
    }
}
