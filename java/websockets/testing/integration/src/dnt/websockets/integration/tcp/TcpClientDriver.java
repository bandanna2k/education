package dnt.websockets.integration.tcp;

import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.client.maybecool.TcpClient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyResponse;
import dnt.websockets.integration.MessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;

public class TcpClientDriver
{
    private final TcpClient client;
    private final MessageCollector collector;

    public TcpClientDriver()
    {
        final ClientMessageProcessor clientMessageProcessor = new ClientMessageProcessor();
        collector = new MessageCollector(this.getClass().getSimpleName(), clientMessageProcessor);
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

    public AbstractMessage getLastMessage()
    {
        return collector.getLastMessage();
    }
}
