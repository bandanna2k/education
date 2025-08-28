package dnt.websockets.integration.vertx;

import dnt.websockets.communications.ServerPushMessage;
import dnt.websockets.server.vertx.WebSocketServer;
import io.vertx.core.Vertx;

public class WebSocketServerDriver
{
    private final WebSocketServer server;

    public WebSocketServerDriver(Vertx vertx)
    {
        server = new WebSocketServer(vertx);
        server.start()
                .onFailure(throwable ->
                {
                    throw new RuntimeException(throwable);
                })
                .toCompletionStage().toCompletableFuture().join();
    }

    public void broadcastMessage()
    {
        server.broadcast(new ServerPushMessage());
    }
}
