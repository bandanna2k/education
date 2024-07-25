package casestudy.bank.publishers;

import education.jackson.response.Response;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class AsyncExecutor
{
    private final Vertx vertx;

    private final Map<UUID, RequestTracking> requests = new HashMap<>();

    private volatile Throwable timeoutResponse;
    private volatile String timeoutMessage;


    public AsyncExecutor(final Vertx vertx)
    {
        this.vertx = vertx;
    }

    public Future<Response> execute(UUID uuid, final Consumer<UUID> asyncRequest)
    {
        final Promise<Response> promise = createAndRegisterPromise(uuid);
        asyncRequest.accept(uuid);
        return promise.future();
    }

    private Promise<Response> createAndRegisterPromise(final UUID uuid)
    {
        final Promise<Response> asyncPromise = Promise.promise();
        final long timerId = vertx.setTimer(15000, id -> timeout(uuid));
        final RequestTracking existingPromise = requests.putIfAbsent(
                uuid,
                new RequestTracking(asyncPromise, vertx.getOrCreateContext(), timerId));
        System.out.println("XXXput"+requests);
        if (existingPromise != null)
        {
            vertx.cancelTimer(timerId);
            throw new IllegalStateException("Request already inflight for Uuid: " + uuid);
        }
        return asyncPromise;
    }

    private void runOnContext(final RequestTracking requestTracking, final Handler<Void> handler)
    {
        requestTracking.context.runOnContext(handler);
    }

    public void onResponseReceived(final Response response)
    {
        final RequestTracking requestTracking = requests.remove(response.uuid);
        if (requestTracking != null)
        {
            vertx.cancelTimer(requestTracking.timerId);
            runOnContext(requestTracking, v -> requestTracking.promise.complete(response));
        }
        else
        {
            System.out.println("XXX ocund not find " + response.uuid);
        }
    }

    private void timeout(final UUID uuid)
    {
        final RequestTracking requestTracking = requests.remove(uuid);
        if (requestTracking != null)
        {
            if (timeoutResponse != null)
            {
                runOnContext(requestTracking, v -> requestTracking.promise.tryFail(timeoutResponse));
            }
            else
            {
                runOnContext(requestTracking, v -> requestTracking.promise.tryFail(
                        Objects.requireNonNullElse(timeoutMessage, "Request timed out with uuid id - " + uuid)));
            }
        }
    }

//    public AsyncExecutor onTimeoutReturn(final Throwable timeoutResponse)
//    {
//        this.timeoutResponse = timeoutResponse;
//        return this;
//    }
//
//    public AsyncExecutor onTimeoutReturn(final String timeoutMessage)
//    {
//        assert timeoutResponse == null;
//        this.timeoutMessage = timeoutMessage;
//        return this;
//    }


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
