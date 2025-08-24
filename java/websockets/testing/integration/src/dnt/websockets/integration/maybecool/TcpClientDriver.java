package dnt.websockets.integration.maybecool;

import dnt.websockets.client.maybecool.TcpClient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyResponse;
import dnt.websockets.integration.PushMessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;

public class TcpClientDriver
{
    private final TcpClient client;
    private final PushMessageCollector collector = new PushMessageCollector();

    public TcpClientDriver()
    {
        client = new TcpClient(collector);
        new Thread(client)
                .start();

        waitForClientToConnect();
    }

    private static void waitForClientToConnect() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return client.getProperty(key)
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
    }

    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return client.setProperty(key, value)
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
    }

    public AbstractMessage popLastMessage()
    {
        return collector.getLastMessage();
    }
}
