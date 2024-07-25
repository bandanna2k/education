package casestudy.bank.publishers;

import education.jackson.response.Response;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.kafka.common.Uuid;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class AsyncExecutor
{
    private final Vertx vertx;

    private final Map<Uuid, RequestTracking> requests = new HashMap<>();

    private volatile Throwable timeoutResponse;
    private volatile String timeoutMessage;


    public AsyncExecutor(final Vertx vertx)
    {
        this.vertx = vertx;
    }

    public Future<Response> execute(final Consumer<Uuid> asyncRequest)
    {
        final Uuid uuid = Uuid.randomUuid();
        final Promise<Response> promise = createAndRegisterPromise(uuid);
        asyncRequest.accept(uuid);
        return promise.future();
    }

    private Promise<Response> createAndRegisterPromise(final Uuid uuid)
    {
        final Promise<Response> asyncPromise = Promise.promise();
        final long timerId = vertx.setTimer(5000, id -> timeout(uuid));
        final RequestTracking existingPromise = requests.putIfAbsent(
                uuid,
                new RequestTracking(asyncPromise, vertx.getOrCreateContext(), timerId));
        if (existingPromise != null)
        {
            vertx.cancelTimer(timerId);
            throw new IllegalStateException("Request already inflight for Uuid: " + uuid);
        }
        return asyncPromise;
    }

    private void onRequestContext(final RequestTracking requestTracking, final Handler<Void> handler)
    {
        requestTracking.context.runOnContext(handler);
    }

    public void onResponse(final Response response, final Handler<Void> handler)
    {
        final RequestTracking record = requests.remove(response.uuid);
        if (record == null)
        {
            System.out.println("Request not found for response: " + response);
        }
        else
        {
            record.context.runOnContext(handler);
            vertx.cancelTimer(record.timerId);

            System.out.printf("Request found for response: Response: %s%n", response);
        }
    }


    private void timeout(final Uuid Uuid)
    {
        final RequestTracking requestTracking = requests.remove(Uuid);
        if (requestTracking != null)
        {
            if (timeoutResponse != null)
            {
                onRequestContext(requestTracking, v -> requestTracking.promise.tryFail(timeoutResponse));
            }
            else
            {
                onRequestContext(requestTracking, v -> requestTracking.promise.tryFail(Objects.requireNonNullElse(timeoutMessage, "Request timed out with correlation id - " + Uuid)));
            }
        }
    }

    public AsyncExecutor onTimeoutReturn(final Throwable timeoutResponse)
    {
        this.timeoutResponse = timeoutResponse;
        return this;
    }

    public AsyncExecutor onTimeoutReturn(final String timeoutMessage)
    {
        assert timeoutResponse == null;
        this.timeoutMessage = timeoutMessage;
        return this;
    }


    private static final class RequestTracking
    {
        private final Promise<Response> promise;
        private final long timerId;
        private final Context context;

        private RequestTracking(final Promise<Response> promise, final Context context, final long timerId)
        {
            this.promise = promise;
            this.timerId = timerId;
            this.context = context;
        }
    }
}
