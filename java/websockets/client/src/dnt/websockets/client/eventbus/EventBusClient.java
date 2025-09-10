package dnt.websockets.client.eventbus;

import dnt.websockets.client.ClientExecutionLayer;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.ClientRequests;
import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.messages.*;
import dnt.websockets.server.eventbus.EventBusPublisher;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static dnt.websockets.vertx.VertxAsyncExecutor.newExecutor;

public class EventBusClient implements ClientRequests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBusClient.class);

    private final MessageVisitor messageVisitor;
    private final Vertx vertx;

    private EventBus eventBus;
    private ClientTextMessageHandler textMessageHandler;
    private ExecutionLayer executorLayer;

    public EventBusClient(Vertx vertx, MessageVisitor messageVisitor)
    {
        this.vertx = vertx;
        this.messageVisitor = messageVisitor;
    }

    public void start()
    {
        final String senderId = String.valueOf(UUID.randomUUID());
        final String clientIncomingTopic = "client.incoming." + senderId;
        final String serverOutgoingTopic = "server.outgoing." + senderId;

        final NetClient netClient = vertx.createNetClient();
        netClient.connect(7779, "localhost")
                .onSuccess(ns ->
                {
                    LOGGER.info("TCP connection established.");
                    this.eventBus = vertx.eventBus();

                    // Register
                    DeliveryOptions deliveryOptions = new DeliveryOptions().addHeader("senderId", senderId);
                    vertx.eventBus().request("client.register", "Please can I register.", deliveryOptions)
                            .onSuccess(reply ->
                            {
                                LOGGER.info("Registration confirmed. " + senderId);
                                subscribe(senderId, clientIncomingTopic, serverOutgoingTopic);
                            }).onFailure(t ->
                            {
                                LOGGER.warn("Failed to register. " + senderId);
                            });
                })
                .onFailure(t -> {
                    LOGGER.warn("Failed to connect. " + senderId);
                })
                .toCompletionStage().toCompletableFuture().join();
    }

    private void subscribe(String senderId, String clientIncomingTopic, String serverOutgoingTopic)
    {
        vertx.eventBus().consumer(serverOutgoingTopic, message ->
        {
            textMessageHandler.handle(message.body().toString());
        });

        DeliveryOptions deliveryOptions = new DeliveryOptions().addHeader("senderId", senderId);
        EventBusPublisher publisher = new EventBusPublisher(eventBus, clientIncomingTopic, deliveryOptions);
        executorLayer = new ClientExecutionLayer(newExecutor(vertx), publisher);
        textMessageHandler = new ClientTextMessageHandler(executorLayer, messageVisitor);
    }

    @Override
    public Future<Result<GetPropertyResponse, String>> getProperty(String key)
    {
        return executorLayer.clientRequestFromServer(new GetPropertyRequest(key));
    }

    @Override
    public Future<Result<SetPropertyResponse, String>> setProperty(String key, String value)
    {
        return executorLayer.clientRequestFromServer(new SetPropertyRequest(key, value));
    }

    public void pushPulse(int rate, long sequence)
    {
        executorLayer.clientSend(new ClientPushPulse(rate, sequence));
    }
}
