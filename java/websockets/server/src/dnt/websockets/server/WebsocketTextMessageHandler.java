package dnt.websockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import dnt.websockets.communications.AbstractRequest;
import dnt.websockets.communications.MessagePublisher;
import dnt.websockets.communications.RequestVisitor;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebsocketTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketTextMessageHandler.class);

    private final ObjectReader messageReader;
    private final MessagePublisher messagePublisher;

    WebsocketTextMessageHandler(ObjectReader messageReader, MessagePublisher messagePublisher)
    {
        this.messageReader = messageReader;
        this.messagePublisher = messagePublisher;
    }

    @Override
    public void handle(String maybeJson)
    {
        try
        {
            RequestVisitor processor = new RequestProcessor(messagePublisher);
            AbstractRequest request = messageReader.readValue(maybeJson);
            request.visit(processor);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }
}
