package dnt.websockets.integration.vertx;

import dnt.websockets.client.Client;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.server.Server;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.http.WebSocket;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTestWithVertx
{
    @Test
    public void shouldSendAndReceive()
    {
        Server server = new Server();
        server.run()
                .toCompletionStage().toCompletableFuture().join();

        Client client = new Client();
        Future<WebSocket> future = client.run()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
        future.toCompletionStage().toCompletableFuture().join();

        Result<OptionsResponse, String> result = client.requestOptions()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()))
                .toCompletionStage().toCompletableFuture().join();
        System.out.println(result);
        assertThat(result.isSuccess()).isTrue();

        client.close();
        server.close();
    }
}
