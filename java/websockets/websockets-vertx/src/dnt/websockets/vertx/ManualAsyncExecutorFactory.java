package dnt.websockets.vertx;

import java.util.concurrent.atomic.AtomicLong;

public abstract class ManualAsyncExecutorFactory
{
    public static <T> ManualAsyncExecutor<T> newExecutor()
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
        return new ManualAsyncExecutor<>(uniqueIdGenerator);
    }
}
