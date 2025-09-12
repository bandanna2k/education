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
        VertxAsyncExecutor<String> executor = new VertxAsyncExecutor.Builder(VERTX).build();

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

    @Test
    void canCompleteInAnyOrder()
    {
        VertxAsyncExecutor<String> executor = new VertxAsyncExecutor.Builder(VERTX).build();

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

    @Test
    void cantCompleteRequestTwice()
    {
        AsyncExecutor<String> executor = new VertxAsyncExecutor<>(VERTX, idGenerator);

        AtomicLong corrId = new AtomicLong();
        Future<String> request = executor.execute(corrId::set);

        executor.onResponseReceived(corrId.get(), "Hello");
        executor.onResponseReceived(corrId.get(), "Hello2");

        String response = request.toCompletionStage().toCompletableFuture().join();
        assertThat(response).isEqualTo("Hello");
    }
}