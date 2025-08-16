package dnt.websockets.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import dnt.websockets.communications.*;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebSocketTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketTextMessageHandler.class);

    private final ObjectReader messageReader;

    WebSocketTextMessageHandler(ObjectReader messageReader)
    {
        this.messageReader = messageReader;
    }

    @Override
    public void handle(String maybeJson)
    {
        LOGGER.debug("Raw input {}", maybeJson);
        try
        {
            ResponseVisitor processor = new ResponseProcessor();
            AbstractResponse request = messageReader.readValue(maybeJson);
            request.visit(processor);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }
}
