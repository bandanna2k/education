package dnt.websockets.client.also;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.Requests;
import dnt.websockets.communications.*;
import dnt.websockets.server.also.EventBusPublisher;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class EventBusClient implements Requests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBusClient.class);

    private final PushMessageVisitor pushMessageVisitor;
    private final Vertx vertx;

    private EventBus eventBus;
    private String topic;
    private ClientTextMessageHandler textMessageHandler;
    private ExecutionLayer executorLayer;

    public EventBusClient(Vertx vertx, PushMessageVisitor pushMessageVisitor)
    {
        this.vertx = vertx;
        this.pushMessageVisitor = pushMessageVisitor;
    }

    public void start()
    {
        String senderId = String.valueOf(UUID.randomUUID());
        vertx.createNetClient()
                .connect(7779, "localhost",
                        ar ->
                        {
                            if (ar.failed())
                            {
                                LOGGER.info("Failed to connect. " + ar.cause());
                                return;
                            }

                            LOGGER.info("TCP connection established.");
                            this.eventBus = vertx.eventBus();
                            this.topic = "client." + senderId;

                            // Register
                            DeliveryOptions deliveryOptions = new DeliveryOptions().addHeader("senderId", senderId);
                            vertx.eventBus().request("client.register", "Please can I register.", deliveryOptions, reply ->
                            {
                                if (reply.failed())
                                {
                                    LOGGER.warn("Failed to register. " + senderId);
                                    return;
                                }

                                LOGGER.info("Registration confirmed. " + senderId);
                                subscribe(senderId);
                            });
                        });
        waitForClientToBeReady();
    }

    private void subscribe(String senderId)
    {
        vertx.eventBus().consumer(topic, message ->
        {
            textMessageHandler.handle(message.body().toString());
        });

        DeliveryOptions deliveryOptions = new DeliveryOptions().addHeader("senderId", senderId);
        EventBusPublisher publisher = new EventBusPublisher(eventBus, "client.request", deliveryOptions);
        executorLayer = new EventBusClientExecutionLayer(vertx, publisher);
        textMessageHandler = new ClientTextMessageHandler(executorLayer, pushMessageVisitor);
    }

    private void waitForClientToBeReady()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return executorLayer.request(new GetPropertyRequest(key));
    }

    @Override
    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return executorLayer.request(new SetPropertyRequest(key, value));
    }
}
