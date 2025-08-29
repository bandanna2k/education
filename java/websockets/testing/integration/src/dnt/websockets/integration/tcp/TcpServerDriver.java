package dnt.websockets.integration.tcp;

import dnt.websockets.messages.ServerPushMessage;
import dnt.websockets.server.ServerMessageProcessor;
import dnt.websockets.server.tcp.TcpServer;

public class TcpServerDriver
{
    private final TcpServer server;

    public TcpServerDriver()
    {
        server = new TcpServer(new ServerMessageProcessor());
        new Thread(server)
                .start();

        waitForServerToStart();
    }

    private static void waitForServerToStart() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcastMessage()
    {
        server.broadcast(new ServerPushMessage());
    }

    public void close()
    {
        server.shutdown();
    }
}
