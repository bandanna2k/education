package dnt.websockets.client.tcp;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.ClientRequests;
import dnt.websockets.client.ClientExecutionLayer;
import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.*;
import dnt.websockets.server.tcp.TcpPublisher;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static dnt.websockets.vertx.VertxAsyncExecutor.newExecutor;
import static dnt.websockets.vertx.VertxFactory.newVertx;

public class TcpClient implements ClientRequests, Runnable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);
    private static final Vertx VERTX = newVertx();

    private final MessageVisitor messageVisitor;

    private ExecutionLayer executorLayer;

    public TcpClient(MessageVisitor messageVisitor)
    {
        this.messageVisitor = messageVisitor;
    }

    @Override
    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return executorLayer.clientRequestFromServer(new GetPropertyRequest(key));
    }

    @Override
    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return executorLayer.clientRequestFromServer(new SetPropertyRequest(key, value));
    }

    @Override
    public void run()
    {
        try
        {
            final Socket socket = new Socket("localhost", 7778);
            LOGGER.info("Connected to server");

            final Publisher publisher = new TcpPublisher(socket);
            executorLayer = new ClientExecutionLayer(newExecutor(VERTX), publisher);

            ClientTextMessageHandler messageHandler = new ClientTextMessageHandler(executorLayer, messageVisitor);
            try(final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())))
            {
                while(true)
                {
                    String maybeJson = reader.readLine();
                    System.out.println(maybeJson);
                    if(maybeJson == null) break;

                    messageHandler.handle(maybeJson);
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
