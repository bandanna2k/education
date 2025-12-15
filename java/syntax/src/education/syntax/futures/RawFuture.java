package education.syntax.futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RawFuture<T>
{
    private final CompletableFuture<T> cf;
    private Consumer<T> onSuccessHandler;
    private Consumer<Throwable> onFailureHandler;
    private Runnable onCompleteHandler;

    private RawFuture(CompletableFuture<T> cf)
    {
        this.cf = cf;
        setupHandlers();
    }

    private void setupHandlers()
    {
        new Thread(() ->
        {
            try
            {
                T result = cf.get();
                if (onSuccessHandler != null)
                {
                    onSuccessHandler.accept(result);
                }
                if (onCompleteHandler != null)
                {
                    onCompleteHandler.run();
                }
            }
            catch (ExecutionException e)
            {
                if (onFailureHandler != null)
                {
                    onFailureHandler.accept(e.getCause());
                }
                if (onCompleteHandler != null)
                {
                    onCompleteHandler.run();
                }
            }
            catch (InterruptedException e)
            {
                if (onFailureHandler != null)
                {
                    onFailureHandler.accept(e);
                }
                if (onCompleteHandler != null)
                {
                    onCompleteHandler.run();
                }
            }
        }).start();
    }

    public RawFuture<T> onSuccess(Consumer<T> handler)
    {
        this.onSuccessHandler = handler;
        return this;
    }

    public RawFuture<T> onFailure(Consumer<Throwable> handler)
    {
        this.onFailureHandler = handler;
        return this;
    }

    public RawFuture<T> onComplete(Runnable handler)
    {
        this.onCompleteHandler = handler;
        return this;
    }

    public static <T> RawFuture<T> succeededFuture(T result)
    {
        CompletableFuture<T> cf = new CompletableFuture<>();
        cf.complete(result);
        return new RawFuture<>(cf);
    }

    public static <T> RawFuture<T> failedFuture(Throwable throwable)
    {
        CompletableFuture<T> cf = new CompletableFuture<>();
        cf.completeExceptionally(throwable);
        return new RawFuture<>(cf);
    }

    public static RawFuture<Void> fromRunnable(Runnable runnable)
    {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        new Thread(() ->
        {
            try
            {
                runnable.run();
                cf.complete(null);
            }
            catch (Exception e)
            {
                cf.completeExceptionally(e);
            }
        }).start();
        return new RawFuture<>(cf);
    }

    public static <T> RawFuture<T> fromSupplier(RawFutureSupplier<T> supplier)
    {
        CompletableFuture<T> cf = new CompletableFuture<>();
        new Thread(() ->
        {
            try
            {
                T result = supplier.get();
                cf.complete(result);
            }
            catch (Exception e)
            {
                cf.completeExceptionally(e);
            }
        }).start();
        return new RawFuture<>(cf);
    }

    public T complete() throws ExecutionException, InterruptedException
    {
        return cf.get();
    }

    @FunctionalInterface
    public interface RawFutureSupplier<T> {
        T get() throws Exception;
    }
}
