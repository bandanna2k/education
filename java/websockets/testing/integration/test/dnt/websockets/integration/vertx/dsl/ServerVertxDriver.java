package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.server.Server;

public class ServerVertxDriver
{
    private final Server server;

    public ServerVertxDriver()
    {
        server = new Server();
        server.run()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()))
                .toCompletionStage().toCompletableFuture().join();
    }
}
