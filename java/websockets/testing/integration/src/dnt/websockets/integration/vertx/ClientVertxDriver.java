package dnt.websockets.integration.vertx;

import dnt.websockets.client.Client;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsResponse;
import education.common.result.Result;
import io.vertx.core.Future;

public class ClientVertxDriver
{
    private final Client client;

    public ClientVertxDriver()
    {
        client = new Client();
        client.run()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()))
                .toCompletionStage().toCompletableFuture().join();
    }

    public Future<Result<OptionsResponse, String>> requestOptions()
    {
        return client.requestOptions()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
    }

    public AbstractMessage popLastMessage()
    {
        return client.popLastMessage();
    }
}
