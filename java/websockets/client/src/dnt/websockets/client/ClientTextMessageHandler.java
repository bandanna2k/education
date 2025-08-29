package dnt.websockets.client;

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

public class ClientTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientTextMessageHandler.class);

    public static final ObjectMapper OBJECT_MAPPER = newClientObjectMapper();
    private static final ObjectReader MESSAGE_READER = getClientMessageReader(OBJECT_MAPPER);

    private final MessageVisitor messageProcessor;
    private final ExecutionLayer executionLayer;

    public ClientTextMessageHandler(ExecutionLayer executionLayer, MessageVisitor messageProcessor)
    {
        this.messageProcessor = messageProcessor;
        this.executionLayer = executionLayer;
    }

    @Override
    public void handle(String maybeJson)
    {
        LOGGER.debug("Client <-- JSON <-- Server | {}", maybeJson);
        try
        {
            AbstractMessage message = MESSAGE_READER.readValue(maybeJson);
            if(message instanceof AbstractResponse response)
            {
                handleResponse(response);
            }
            handle(message);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }

    private void handleResponse(AbstractResponse response)
    {
        executionLayer.serverResponseToRequest(response);
    }

    private void handle(AbstractMessage message)
    {
        message.visit(executionLayer, messageProcessor);
    }

    private static ObjectMapper newClientObjectMapper()
    {
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.registerSubtypes(new NamedType(GetPropertyResponse.class, GetPropertyResponse.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(SetPropertyResponse.class, SetPropertyResponse.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(ServerPushMessage.class, ServerPushMessage.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(ErrorResponse.class, ErrorResponse.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(GetStatusRequest.class, GetStatusRequest.class.getSimpleName()));
        return mapper;
    }
    private static ObjectReader getClientMessageReader(ObjectMapper mapper)
    {
        return mapper.readerFor(AbstractMessage.class);
    }
}
