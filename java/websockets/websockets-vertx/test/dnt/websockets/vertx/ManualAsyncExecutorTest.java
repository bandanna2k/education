package dnt.websockets.vertx;

import io.vertx.core.Future;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import static dnt.websockets.vertx.VertxAsyncExecutorFactory.newExecutor;
import static org.assertj.core.api.Assertions.assertThat;

class ManualAsyncExecutorTest
{
    private long nextId = 1;
    private final UniqueIdGenerator idGenerator = () -> nextId++;

    @Test
    void shouldCompleteExecutor()
    {
        ManualAsyncExecutor<String> executor = new ManualAsyncExecutor<>(idGenerator);

        AtomicLong corrId = new AtomicLong();
        Future<String> request = executor.execute(corrId::set);

        executor.onResponseReceived(corrId.get(), "Hello");

        String response = request.toCompletionStage().toCompletableFuture().join();
        assertThat(response).isEqualTo("Hello");
    }

    @Test
    void shouldNotCompleteExecutor()
    {
        ManualAsyncExecutor<String> executor = new ManualAsyncExecutor<>(idGenerator);

        AtomicLong corrId = new AtomicLong();
        Future<String> request = executor.execute(corrId::set);

        Assertions.assertThatExceptionOfType(ConditionTimeoutException.class)
                        .isThrownBy(() -> Awaitility.await()
                                .atMost(Duration.ofMillis(500))
                                .until(() -> {
                                    String response = request.toCompletionStage().toCompletableFuture().join();
                                    return true; // This return won't be reached if method times out
                                }));
    }

    @Test
    void canCompleteInAnyOrder()
    {
        AsyncExecutor<String> executor = ManualAsyncExecutorFactory.newExecutor();

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