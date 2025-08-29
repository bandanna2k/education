package dnt.websockets.integration.vertx;

import dnt.websockets.messages.ServerPushMessage;
import dnt.websockets.server.vertx.WebSocketServer;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class WebSocketServerDriver
{
    private final WebSocketServer server;

    public WebSocketServerDriver(Vertx vertx)
    {
        server = new WebSocketServer(vertx);
    }

    public Future<HttpServer> start()
    {
        return server.start();
    }

    public void broadcastMessage()
    {
        server.broadcast(new ServerPushMessage());
    }
}
