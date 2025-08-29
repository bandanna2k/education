package dnt.websockets.integration.infrastructure;

import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.function.Supplier;

class DeferredFuture<T>
{
    private final Promise<T> promise;
    private final Supplier<T> supplier;

    public DeferredFuture(Supplier<T> supplier)
    {
        this.promise = Promise.promise();
        this.supplier = supplier;
    }

    public Future<T> future()
    {
        return promise.future();
    }

    public void complete()
    {
        try
        {
            T result = supplier.get();
            promise.complete(result);
        }
        catch (Exception e)
        {
            promise.fail(e);
        }
    }
}
