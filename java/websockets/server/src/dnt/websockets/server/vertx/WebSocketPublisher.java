package dnt.websockets.server.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.AbstractMessage;
import io.vertx.core.http.WebSocketBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketPublisher implements Publisher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketPublisher.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE = 102;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final WebSocketBase serverWebSocket;

    public WebSocketPublisher(WebSocketBase serverWebSocket)
    {
        this.serverWebSocket = serverWebSocket;
    }

    @Override
    public void send(AbstractMessage message)
    {
        LOGGER.debug("Client --> Pojo     Server | {}", message);
        try
        {
            serverWebSocket.writeTextMessage(OBJECT_MAPPER.writeValueAsString(message));
        }
        catch (JsonProcessingException e)
        {
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE);
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
