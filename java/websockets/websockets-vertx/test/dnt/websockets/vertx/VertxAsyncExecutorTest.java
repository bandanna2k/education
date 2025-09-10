package dnt.websockets.vertx;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicLong;

import static dnt.websockets.vertx.VertxFactory.newVertx;
import static org.assertj.core.api.Assertions.assertThat;

class VertxAsyncExecutorTest
{
    private static Vertx VERTX = newVertx();

    private long nextId = 1;
    private UniqueIdGenerator idGenerator = () -> nextId++;

    @Test
    void shouldCompleteExecutor()
    {
        VertxAsyncExecutor<String> executor = new VertxAsyncExecutor<>(VERTX, idGenerator);

        AtomicLong corrId = new AtomicLong();
        Future<String> request = executor.execute(corrId::set);

        executor.onResponseReceived(corrId.get(), "Hello");

        String response = request.toCompletionStage().toCompletableFuture().join();
        assertThat(response).isEqualTo("Hello");
    }

    @Test
    void shouldNotCompleteExecutor()
    {
        VertxAsyncExecutor<String> executor = new VertxAsyncExecutor<>(VERTX, idGenerator, 1_000);

        AtomicLong corrId = new AtomicLong();
        Future<String> request = executor.execute(corrId::set);

        Assertions.assertThatExceptionOfType(CompletionException.class)
                .isThrownBy(() -> {
                    String response = request.toCompletionStage().toCompletableFuture().join();
                });
    }
}