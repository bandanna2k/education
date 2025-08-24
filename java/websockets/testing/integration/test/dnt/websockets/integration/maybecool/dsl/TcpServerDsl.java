package dnt.websockets.integration.maybecool.dsl;

import dnt.websockets.integration.maybecool.TcpServerDriver;

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
