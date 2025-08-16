package dnt.websockets.server.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.WebSocketBase;

public class MessagePublisher
{
    private static final short WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE = 102;

    private final WebSocketBase serverWebSocket;
    private final ObjectMapper objectMapper;
    private long nextCorrelationId = 1;

    public MessagePublisher(WebSocketBase serverWebSocket, ObjectMapper objectMapper)
    {
        this.serverWebSocket = serverWebSocket;
        this.objectMapper = objectMapper;
    }

    public void send(AbstractMessage message)
    {
        try
        {
            serverWebSocket.writeTextMessage(objectMapper.writeValueAsString(message));
        }
        catch (JsonProcessingException e)
        {
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE);
            throw new RuntimeException(e);
        }
    }

    public synchronized long getNextCorrelationId()
    {
        return nextCorrelationId++;
    }
}
