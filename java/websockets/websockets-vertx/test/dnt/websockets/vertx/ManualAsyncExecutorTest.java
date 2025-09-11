package dnt.websockets.vertx;

import io.vertx.core.Future;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class ManualAsyncExecutorTest
{
    private long nextId = 1;
    private final UniqueIdGenerator idGenerator = () -> nextId++;

    @Test
    void shouldCompleteExecutor()
    {
        try(ManualAsyncExecutor<String> executor = new ManualAsyncExecutor<>(idGenerator))
        {
            AtomicLong corrId = new AtomicLong();
            Future<String> request = executor.execute(corrId::set);

            executor.onResponseReceived(corrId.get(), "Hello");

            String response = request.toCompletionStage().toCompletableFuture().join();
            assertThat(response).isEqualTo("Hello");
        }
    }

    @Test
    void shouldNotCompleteExecutor()
    {
        Assertions.assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    try(ManualAsyncExecutor<String> executor = new ManualAsyncExecutor<>(idGenerator))
                    {
                        AtomicLong corrId = new AtomicLong();
                        Future<String> request = executor.execute(corrId::set);

                        // Don't respond
                    }
                })
                .withMessage("Unexpected promises not complete. Count: 1");
    }

    @Test
    void canCompleteInAnyOrder()
    {
        try(ManualAsyncExecutor<String> executor = new ManualAsyncExecutor<>(idGenerator))
        {
            AtomicLong corrId1 = new AtomicLong();
            Future<String> request1 = executor.execute(corrId1::set);

            AtomicLong corrId2 = new AtomicLong();
            Future<String> request2 = executor.execute(corrId2::set);

            executor.onResponseReceived(corrId2.get(), "2");

            String response2 = request2.toCompletionStage().toCompletableFuture().join();
            assertThat(response2).isEqualTo("2");

            executor.onResponseReceived(corrId1.get(), "1");

            String response1 = request1.toCompletionStage().toCompletableFuture().join();
            assertThat(response1).isEqualTo("1");
        }
    }

    @Test
    void cantCompleteRequestTwice()
    {
        AtomicLong countOfNotFound = new AtomicLong(0);
        try (ManualAsyncExecutor<String> executor = new ManualAsyncExecutor<String>(idGenerator)
                .withHandlerForPromiseNotFound(aLong -> countOfNotFound.incrementAndGet()))
        {

            AtomicLong corrId = new AtomicLong();
            Future<String> request = executor.execute(corrId::set);

            executor.onResponseReceived(corrId.get(), "Hello");
            executor.onResponseReceived(corrId.get(), "Hello2");

            String response = request.toCompletionStage().toCompletableFuture().join();
            assertThat(response).isEqualTo("Hello");
            assertThat(countOfNotFound.get()).isEqualTo(1);
        }
    }
}