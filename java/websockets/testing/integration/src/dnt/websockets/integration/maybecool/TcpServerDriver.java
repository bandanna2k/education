package dnt.websockets.integration.maybecool;

import dnt.websockets.communications.ServerPushMessage;
import dnt.websockets.server.maybecool.TcpServer;

public class TcpServerDriver
{
    private final TcpServer server;

    public TcpServerDriver()
    {
        server = new TcpServer();
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
