package dnt.websockets.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import dnt.websockets.communications.*;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

class WebSocketTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketTextMessageHandler.class);

    private final ObjectReader messageReader;
    private final WebSocketExecutorLayer executorLayer;
    private final List<Listener> listeners;

    WebSocketTextMessageHandler(ObjectReader messageReader, WebSocketExecutorLayer executorLayer, Listener... listeners)
    {
        this.messageReader = messageReader;
        this.executorLayer = executorLayer;
        this.listeners = Arrays.asList(listeners);
    }

    @Override
    public void handle(String maybeJson)
    {
        LOGGER.debug("Raw input {}", maybeJson);
        try
        {
            AbstractMessage message = messageReader.readValue(maybeJson);
            if(message instanceof AbstractResponse)
            {
                ResponseVisitor processor = new ResponseProcessor(executorLayer);
                ((AbstractResponse)message).visit(processor);
                return;
            }

            listeners.forEach(listener -> listener.onMessage(message));
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }

    interface Listener
    {
        void onMessage(AbstractMessage message);
    }
}
