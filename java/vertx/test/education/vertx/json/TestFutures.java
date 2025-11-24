package education.vertx.json;

import io.vertx.core.Future;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class TestFutures
{
    @Test
    public void testFutureExecution()
    {
        Future<String> future1 = Future.future(stringPromise -> {
            sleep(1000);
            log("1 second future finished");
            stringPromise.complete("Future 1");
        });

        Future<String> future2 = Future.future(stringPromise -> {
            sleep(2000);
            log("2 second future finished");
            stringPromise.complete("Future 2");
        });

        Future.all(future1, future2)
                .onSuccess(compositeFuture -> {
                    Object o = compositeFuture.resultAt(0);
                    Object o1 = compositeFuture.resultAt(1);
                    log(o);
                    log(o1);
                    log("Both futures completed");
                })
                .onFailure(Throwable::printStackTrace)
                .toCompletionStage().toCompletableFuture().join();
    }

    private static void sleep(int durationMillis)
    {
        log("Sleeping for " + durationMillis + " milliseconds");
        try {
            Thread.sleep(durationMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void log(Object message) {
        Instant now = Instant.now();
        System.out.printf("%s %s%n", now, message);
    }
}
