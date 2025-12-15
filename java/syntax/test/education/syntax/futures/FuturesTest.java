package education.syntax.futures;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

public class FuturesTest
{
    @Test
    public void testFutures()
    {
        try (ExecutorService executor = Executors.newFixedThreadPool(2))
        {
            final long duration = 5000;
            Future<Long> future1 = executor.submit(() -> sleep(duration));
            Future<Long> future2 = executor.submit(() -> sleep(duration));
            System.out.println(future1.isDone());
            System.out.println(future2.isDone());

            System.out.println("Before: Future 1 result: " + future1.get());
            System.out.println("Should output between future result: " + future1.get());
            System.out.println("After: Future 2 result: " + future2.resultNow());
        }
        catch (ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static long sleep(long duration)
    {
        try
        {
            long periods = duration / 1000;
            for (int i = 1; i <= periods; i++)
            {
                String threadName = Thread.currentThread().getName();
                System.out.printf("[%s] Sleeping %d%n", threadName, i);
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        return duration;
    }


}
