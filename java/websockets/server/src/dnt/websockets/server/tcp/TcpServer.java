package dnt.websockets.server.tcp;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.AbstractResponse;
import dnt.websockets.messages.MessageVisitor;
import dnt.websockets.server.ServerExecutionLayer;
import dnt.websockets.server.ServerMessageProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import dnt.websockets.vertx.VertxAsyncExecutor;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public class TcpServer implements Runnable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);
    private static final Vertx VERTX = newVertx();

    private final List<ExecutionLayer> executionLayers = new ArrayList<>();
    private final MessageVisitor requestProcessor;

    private boolean acceptConnections = true;
    private ServerSocket serverSocket;

    public TcpServer(ServerMessageProcessor requestProcessor)
    {
        this.requestProcessor = requestProcessor;
    }

    @Override
    public void run()
    {
        try
        {
            serverSocket = new ServerSocket(7778);
            LOGGER.info("Server started");

            while (acceptConnections)
            {
                final Socket socket = serverSocket.accept();
                new Thread(() -> handle(socket)).start();
            }
        }
        catch (IOException e)
        {
            // Do nothing
        }

        finally
        {
            LOGGER.info("Server stopped.");
        }
    }

    private void handle(Socket socket)
    {
        try(final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            final Publisher messagePublisher = new TcpPublisher(socket);
            final VertxAsyncExecutor<AbstractResponse> executor = new VertxAsyncExecutor.Builder(VERTX).timeoutMillis(2_000L).build();
            final ExecutionLayer executorLayer = new ServerExecutionLayer(executor, messagePublisher); // Use vertx for now.
            final ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executorLayer, requestProcessor);
            executionLayers.add(executorLayer);

            LOGGER.info("Server accepted client connection. {}", socket.getRemoteSocketAddress());

            while(true)
            {
                String maybeJson = reader.readLine();
                System.out.println(maybeJson);
                if(maybeJson == null) break;

                textMessageHandler.handle(maybeJson);
            }
        }
        catch (IOException e)
        {
            // Do nothing
        }
        finally
        {
            LOGGER.info("Server closed client connection.");
        }
    }

    public void broadcast(AbstractMessage message)
    {
        executionLayers.forEach(executionLayer -> executionLayer.serverSend(message));
    }

    public void shutdown()
    {
        if (serverSocket != null)
        {
            try
            {
                acceptConnections = false;
                serverSocket.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
