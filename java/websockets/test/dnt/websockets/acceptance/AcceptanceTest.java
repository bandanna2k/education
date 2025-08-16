package dnt.websockets.acceptance;

import dnt.websockets.client.Client;
import dnt.websockets.server.Server;
import io.vertx.core.Future;
import io.vertx.core.http.WebSocket;
import org.junit.Test;

public class AcceptanceTest
{
    @Test
    public void shouldSendAndReceive()
    {
        Server server = new Server();
        server.go()
                .toCompletionStage().toCompletableFuture().join();

        Client client = new Client();
        Future<WebSocket> future = client.go()
                .onSuccess(unused ->
                {
                    client.requestOptions();
                })
                .onComplete(unused ->
                {
                    client.close();
                    server.close();
                });
        future.toCompletionStage().toCompletableFuture().join();
    }
}
