package dnt.websockets.server.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.Publisher;
import io.vertx.core.http.WebSocketBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxPublisher implements Publisher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxPublisher.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE = 102;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final WebSocketBase serverWebSocket;

    public VertxPublisher(WebSocketBase serverWebSocket)
    {
        this.serverWebSocket = serverWebSocket;
    }

    @Override
    public void send(AbstractMessage message)
    {
        LOGGER.debug("Sending {}", message);
        try
        {
            String s = OBJECT_MAPPER.writeValueAsString(message);
            System.out.println(s);
            serverWebSocket.writeTextMessage(s);
        }
        catch (JsonProcessingException e)
        {
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE);
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
