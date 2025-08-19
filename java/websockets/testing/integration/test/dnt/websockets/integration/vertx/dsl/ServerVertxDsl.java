package dnt.websockets.integration.vertx.dsl;

import io.vertx.core.Future;

public class ServerVertxDsl
{
    private final ServerVertxDriver serverDriver = new ServerVertxDriver();

    private <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }
}
