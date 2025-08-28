package dnt.websockets.integration.tcp.dsl;

import dnt.websockets.integration.tcp.TcpServerDriver;

public class TcpServerDsl
{
    private final TcpServerDriver serverDriver;

    public TcpServerDsl(TcpServerDriver serverDriver)
    {
        this.serverDriver = serverDriver;
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
    }
}
