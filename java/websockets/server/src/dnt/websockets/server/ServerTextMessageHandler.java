package dnt.websockets.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.messages.*;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTextMessageHandler.class);

    public static final ObjectMapper OBJECT_MAPPER = newServerObjectMapper();
    private static final ObjectReader MESSAGE_READER = getServerMessageReader(OBJECT_MAPPER);

    private final MessageVisitor processor;

    private final ExecutionLayer executionLayer;

    public ServerTextMessageHandler(ExecutionLayer executionLayer, MessageVisitor requestProcessor)
    {
        this.processor = requestProcessor;
        this.executionLayer = executionLayer;
    }

    @Override
    public void handle(String maybeJson)
    {
        LOGGER.debug("Client --> JSON --> Server | {}", maybeJson);
        try
        {
            AbstractMessage message = MESSAGE_READER.readValue(maybeJson);
            if(message instanceof AbstractResponse response)
            {
                handleResponse(response);
                return;
            }

            handle(MESSAGE_READER.<AbstractMessage>readValue(maybeJson));
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }

    private void handleResponse(AbstractResponse response)
    {
        executionLayer.clientResponseToRequest(response);
    }

    public void handle(AbstractMessage message)
    {
        message.visit(executionLayer, processor);
    }

    public void send(AbstractMessage message)
    {
        LOGGER.debug("Client     Pojo <--- Server | Sending {}", message);
        executionLayer.serverSend(message);
    }

    private static ObjectMapper newServerObjectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.registerSubtypes(new NamedType(GetPropertyRequest.class, GetPropertyRequest.class.getSimpleName()));
        objectMapper.registerSubtypes(new NamedType(SetPropertyRequest.class, SetPropertyRequest.class.getSimpleName()));
        objectMapper.registerSubtypes(new NamedType(ClientPushPulse.class, ClientPushPulse.class.getSimpleName()));
        objectMapper.registerSubtypes(new NamedType(GetStatusResponse.class, GetStatusResponse.class.getSimpleName()));
        objectMapper.registerSubtypes(new NamedType(ErrorResponse.class, ErrorResponse.class.getSimpleName()));
        return objectMapper;
    }

    private static ObjectReader getServerMessageReader(ObjectMapper objectMapper)
    {
        return objectMapper.readerFor(AbstractMessage.class);
    }
}
