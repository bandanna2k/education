package acceptance;

import dnt.websockets.client.Client;
import dnt.websockets.server.Server;
import io.vertx.core.Future;
import io.vertx.core.http.WebSocket;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcceptanceTest
{
    @Test
    public void shouldSendAndReceive() throws InterruptedException
    {
        Server server = new Server();
        server.run()
                .toCompletionStage().toCompletableFuture().join();

        Client client = new Client();
        Future<WebSocket> future = client.run()
                .onSuccess(unused ->
                {
                    client.requestOptions();
                })
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
        future.toCompletionStage().toCompletableFuture().join();

        Thread.sleep(1000);

        client.close();
        server.close();
    }
}
