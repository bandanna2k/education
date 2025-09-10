package dnt.websockets.vertx;

import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.HashMap;
import java.util.Map;

class ManualAsyncExecutor<Response> implements AsyncExecutor<Response>
{
    private final UniqueIdGenerator idGenerator;
    private final Map<Long, Promise<Response>> correlationIdToPromise = new HashMap<>();

    public ManualAsyncExecutor(UniqueIdGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }

    @Override
    public Future<Response> execute(AsyncRequest asyncRequest)
    {
        final long correlationId = this.idGenerator.generateId();

        Promise<Response> promise = Promise.promise();
        correlationIdToPromise.put(correlationId, promise);

        asyncRequest.invoke(correlationId);
        return promise.future();
    }

    @Override
    public void onResponseReceived(long correlationId, Response response)
    {
        Promise<Response> promise = correlationIdToPromise.get(correlationId);
        if (promise == null)
        {
            handlePromiseNotFound(correlationId);
        }
        else
        {
            promise.complete(response);
        }
    }

    protected void handlePromiseNotFound(long correlationId)
    {
        throw new RuntimeException("Request not found for correlation ID: " + correlationId);
    }
}
