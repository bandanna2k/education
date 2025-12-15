package education.syntax.futures;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RawFuturesTest
{
    @Test
    public void testFutures() throws ExecutionException, InterruptedException
    {
        RawFuture<Long> future = RawFuture.fromSupplier(() -> {
                    long duration = 3000;
                    return sleep(duration);
                })
                .onSuccess(System.out::println)
                .onFailure(System.err::println)
                .onComplete(() -> System.out.println("Complete"));
        System.out.println("Call complete method: " + future.complete());
    }

    @Test
    public void testInterrupt() throws ExecutionException, InterruptedException
    {
        List<Object> list = new ArrayList<>();
        RawFuture<Long> future = RawFuture.fromSupplier(() -> {
                    long duration = 3000;
                    if(!list.isEmpty()) {
                        throw new Exception("Unexpected item found.");
                    }
                    return sleep(duration);
                })
                .onSuccess(System.out::println)
                .onFailure(System.err::println)
                .onComplete(() -> System.out.println("Complete"));
        list.add(1);
        Assertions.assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> System.out.println("Calling complete returns: " + future.complete()));
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
