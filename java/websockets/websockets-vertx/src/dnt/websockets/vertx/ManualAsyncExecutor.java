package dnt.websockets.vertx;

import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ManualAsyncExecutor<Response> implements AsyncExecutor<Response>
{
    private final UniqueIdGenerator idGenerator;
    private final Map<Long, Promise<Response>> correlationIdToPromise = new HashMap<>();

    private Consumer<Long> handlerForPromiseNotFound = correlationId -> {};

    public ManualAsyncExecutor(UniqueIdGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }

    public ManualAsyncExecutor<Response> withHandlerForPromiseNotFound(Consumer<Long> handler)
    {
        this.handlerForPromiseNotFound = handler;
        return this;
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
        Promise<Response> promise = correlationIdToPromise.remove(correlationId);
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
        this.handlerForPromiseNotFound.accept(correlationId);
    }
}
