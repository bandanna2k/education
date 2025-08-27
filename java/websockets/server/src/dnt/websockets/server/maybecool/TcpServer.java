package dnt.websockets.server.maybecool;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.Publisher;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import dnt.websockets.server.ServerExecutionLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpServer implements Runnable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    private final List<ExecutionLayer> executionLayers = new ArrayList<>();
    private final RequestProcessor requestProcessor = new RequestProcessor();

    private boolean acceptConnections = true;
    private ServerSocket serverSocket;

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
            Publisher messagePublisher = new TcpPublisher(socket);
            ExecutionLayer executorLayer = new ServerExecutionLayer(messagePublisher); // Use vertx for now.
            ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executorLayer, requestProcessor);
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
        executionLayers.forEach(executionLayer -> executionLayer.send(message));
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
