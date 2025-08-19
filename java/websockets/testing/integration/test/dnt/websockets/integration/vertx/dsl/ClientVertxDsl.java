package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsResponse;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import org.assertj.core.api.Assertions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientVertxDsl
{
    private final ClientVertxDriver clientDriver;

    public ClientVertxDsl(ClientVertxDriver clientDriver)
    {
        this.clientDriver = clientDriver;
    }

    public void fetchOptions()
    {
        Result<OptionsResponse, String> result = join(clientDriver.requestOptions());
        System.out.println(result);
        assertThat(result.isSuccess()).isTrue();
    }

    private <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }

    public void verifyMessage(String className)
    {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                AbstractMessage message = clientDriver.popLastMessage();
                while(message == null || !message.getClass().getSimpleName().equalsIgnoreCase(className))
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    message = clientDriver.popLastMessage();
                }
            }
        });

        Assertions.assertThat(completableFuture)
                .succeedsWithin(5, TimeUnit.SECONDS);
    }
}
