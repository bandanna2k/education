package dnt.websockets.integration.vertx;

import dnt.websockets.integration.MessageCollector;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.GetStatusResponse;
import dnt.websockets.messages.ServerPushMessage;
import dnt.websockets.server.ServerMessageProcessor;
import dnt.websockets.server.vertx.WebSocketServer;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class WebSocketServerDriver
{
    private final ServerMessageProcessor serverMessageProcessor = new ServerMessageProcessor();
    private final MessageCollector collector = new MessageCollector(this.getClass().getSimpleName(), serverMessageProcessor);

    private final WebSocketServer server;

    public WebSocketServerDriver(Vertx vertx)
    {
        server = new WebSocketServer(vertx, collector);
    }

    public Future<HttpServer> start()
    {
        return server.start();
    }

    public void broadcastMessage()
    {
        server.broadcast(new ServerPushMessage());
    }

    public Future<Result<GetStatusResponse, String>> getStatusFromClient(String status)
    {
        return server.getStatus(status);
    }

    public AbstractMessage popLastMessage()
    {
        return collector.getLastMessage();
    }

    public void clearMessages()
    {
        collector.clear();
    }
}
