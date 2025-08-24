package dnt.websockets.server.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ErrorResponse;
import dnt.websockets.communications.Publisher;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxRestPublisher implements Publisher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxRestPublisher.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RoutingContext routingContext;

    public VertxRestPublisher(RoutingContext ctx)
    {
        this.routingContext = ctx;
    }

    @Override
    public void send(AbstractMessage message)
    {
        LOGGER.debug("Sending {}", message);
        try
        {
            if(message instanceof ErrorResponse restResponse)
            {
                String response = OBJECT_MAPPER.writeValueAsString(message);
                routingContext.response()
                        .setStatusCode(restResponse.statusCode)
                        .send(response);
                return;
            }
            String response = OBJECT_MAPPER.writeValueAsString(message);
            routingContext.response()
                    .setStatusCode(200)
                    .send(response);
        }
        catch (JsonProcessingException e)
        {
            routingContext.response()
                    .setStatusCode(400)
                    .send(e.getMessage());
        }
    }
}
