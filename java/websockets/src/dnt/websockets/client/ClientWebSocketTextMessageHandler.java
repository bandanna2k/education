package dnt.websockets.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.infrastructure.MessagePublisher;
import dnt.websockets.server.infrastructure.AbstractRequest;
import dnt.websockets.server.infrastructure.RequestVisitor;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ClientWebSocketTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientWebSocketTextMessageHandler.class);

    private final ObjectReader messageReader;
    private final MessagePublisher messagePublisher;

    ClientWebSocketTextMessageHandler(ObjectReader messageReader, MessagePublisher messagePublisher)
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
