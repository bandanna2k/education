package dnt.websockets.integration.tcp.dsl;

import dnt.websockets.integration.tcp.TcpClientDriver;
import dnt.websockets.integration.tcp.TcpServerDriver;
import org.junit.AfterClass;

public abstract class AbstractIntegrationTcpTest
{
    private static final TcpServerDriver serverDriver = new TcpServerDriver();
    private static final TcpClientDriver clientDriver = new TcpClientDriver();

    protected TcpServerDsl server = new TcpServerDsl(serverDriver);
    protected TcpClientDsl client = new TcpClientDsl(clientDriver);

    @AfterClass
    public static void afterClass()
    {
        serverDriver.close();
    }
}
