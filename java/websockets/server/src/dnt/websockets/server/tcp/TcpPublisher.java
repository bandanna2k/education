package dnt.websockets.server.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.infrastructure.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class TcpPublisher implements Publisher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpPublisher.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final PrintWriter writer;

    public TcpPublisher(Socket socket) throws IOException
    {
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void send(AbstractMessage message)
    {
        LOGGER.debug("Sending {}", message);
        try
        {
            writer.println(OBJECT_MAPPER.writeValueAsString(message));
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
