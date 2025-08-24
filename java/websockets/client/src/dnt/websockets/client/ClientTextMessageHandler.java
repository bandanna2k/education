package dnt.websockets.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import dnt.websockets.communications.*;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientTextMessageHandler.class);

    public static final ObjectMapper OBJECT_MAPPER = newClientObjectMapper();
    private static final ObjectReader MESSAGE_READER = getClientMessageReader(OBJECT_MAPPER);

    private final ResponseVisitor processor;
    private final PushMessageVisitor pushMessageProcessor;

    public ClientTextMessageHandler(ExecutionLayer executionLayer, PushMessageVisitor pushMessageProcessor)
    {
        this.pushMessageProcessor = pushMessageProcessor;
        this.processor = new ResponseProcessor(executionLayer);
    }

    @Override
    public void handle(String maybeJson)
    {
        LOGGER.debug("Raw input {}", maybeJson);
        try
        {
            AbstractMessage message = MESSAGE_READER.readValue(maybeJson);
            if(message instanceof AbstractResponse)
            {
                handle((AbstractResponse)message);
                return;
            }

            handle(message);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }

    private void handle(AbstractResponse response)
    {
        response.visit(processor);
    }

    private void handle(AbstractMessage message)
    {
        message.visit(pushMessageProcessor);
    }

    private static ObjectMapper newClientObjectMapper()
    {
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.registerSubtypes(new NamedType(GetPropertyResponse.class, GetPropertyResponse.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(SetPropertyResponse.class, SetPropertyResponse.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(PushMessage.class, PushMessage.class.getSimpleName()));
        return mapper;
    }
    private static ObjectReader getClientMessageReader(ObjectMapper mapper)
    {
        return mapper.readerFor(AbstractMessage.class);
    }
}
