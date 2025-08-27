package dnt.websockets.client.maybecool;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.Requests;
import dnt.websockets.client.ClientExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.maybecool.TcpPublisher;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public class TcpClient implements Requests, Runnable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);
    private static final Vertx VERTX = newVertx();

    private final PushMessageVisitor pushMessageVisitor;

    private ExecutionLayer executorLayer;

    public TcpClient(PushMessageVisitor pushMessageVisitor)
    {
        this.pushMessageVisitor = pushMessageVisitor;
    }

    @Override
    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return executorLayer.request(new GetPropertyRequest(key));
    }

    @Override
    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return executorLayer.request(new SetPropertyRequest(key, value));
    }

    @Override
    public void run()
    {
        try
        {
            final Socket socket = new Socket("localhost", 7778);
            LOGGER.info("Connected to server");

            final Publisher publisher = new TcpPublisher(socket);
            executorLayer = new ClientExecutionLayer(newExecutor(), publisher);

            ClientTextMessageHandler messageHandler = new ClientTextMessageHandler(executorLayer, pushMessageVisitor);
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

    private static VertxAsyncExecutor<AbstractResponse> newExecutor()
    {
        final VertxAsyncExecutor.UniqueIdGenerator uniqueIdGenerator = new VertxAsyncExecutor.UniqueIdGenerator()
        {
            private final AtomicLong nextCorrelationId = new AtomicLong(1);

            @Override
            public long generateId()
            {
                return nextCorrelationId.getAndIncrement();
            }
        };
        return new VertxAsyncExecutor<>(VERTX, uniqueIdGenerator);
    }
}
