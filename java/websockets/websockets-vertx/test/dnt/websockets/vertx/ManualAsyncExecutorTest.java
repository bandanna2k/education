package dnt.websockets.vertx;

import io.vertx.core.Future;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

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
}