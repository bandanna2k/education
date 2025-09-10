package dnt.websockets.vertx;

import io.vertx.core.Vertx;

import java.util.concurrent.atomic.AtomicLong;

public abstract class VertxAsyncExecutorFactory
{
    public static <T> VertxAsyncExecutor<T> newExecutor(Vertx vertx)
    {
        final UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator()
        {
            private final AtomicLong nextCorrelationId = new AtomicLong(System.currentTimeMillis() % 100_000);

            @Override
            public long generateId()
            {
                return nextCorrelationId.getAndIncrement();
            }
        };
        return new VertxAsyncExecutor<>(vertx, uniqueIdGenerator);
    }
}
