package dnt.websockets.server.also;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.Publisher;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventBusPublisher implements Publisher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBusPublisher.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final EventBus eventBus;
    private final String topic;
    private final DeliveryOptions deliveryOptions;

    public EventBusPublisher(EventBus eventBus, String topic, DeliveryOptions deliveryOptions)
    {
        this.eventBus = eventBus;
        this.topic = topic;
        this.deliveryOptions = deliveryOptions;
    }

    @Override
    public void send(AbstractMessage message)
    {
        LOGGER.debug("Sending {}", message);
        try
        {
            String s = OBJECT_MAPPER.writeValueAsString(message);
            System.out.println(s);
            eventBus.send(topic, s, deliveryOptions);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
