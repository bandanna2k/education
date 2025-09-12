package dnt.websockets.server.eventbus;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.AbstractResponse;
import dnt.websockets.server.ServerExecutionLayer;
import dnt.websockets.server.ServerMessageProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import dnt.websockets.vertx.VertxAsyncExecutor;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EventBusServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBusServer.class);

    private final List<ExecutionLayer> executionLayersForBroadcast = new ArrayList<>();
    private final Vertx vertx;
    private final ServerMessageProcessor requestProcessor = new ServerMessageProcessor();
    private final Map<String, String> registeredClients = new HashMap<>();
    private final Map<String, ServerTextMessageHandler> senderIdToTextMessageHandler = new HashMap<>();

    public EventBusServer(Vertx vertx)
    {
        this.vertx = vertx;
    }

    public void start()
    {
        BridgeOptions options = new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("client.register"))
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("client\\..*"));


        TcpEventBusBridge.create(vertx, options)
                .listen(7779)
                .onSuccess(unused -> {
                    LOGGER.info("TCP Event Bus Bridge listening.");
                })
                .onFailure(throwable -> {
                    throw new RuntimeException("Failed to start bridge: " + throwable.getMessage());
                });

        // Handle client registrations
        final EventBus eventBus = vertx.eventBus();
        eventBus.consumer("client.register", message -> {

            if(!message.body().toString().toLowerCase().contains("please"))
            {
                message.fail(401, "Didn't say please.");
                return;
            }

            String senderId = message.headers().get("senderId");
            String clientIncomingTopic = "client.incoming." + senderId;
            String serverOutgoingTopic = "server.outgoing." + senderId;
            registeredClients.put(senderId, serverOutgoingTopic);

            LOGGER.info("Client registered: " + senderId + " -> " + serverOutgoingTopic);

            eventBus.send(serverOutgoingTopic, "Welcome. Thank you.");
            message.reply("Registered. Thank you.");

            final DeliveryOptions deliveryOptions = new DeliveryOptions().addHeader("senderId", senderId);
            final Publisher publisher = new EventBusPublisher(eventBus, serverOutgoingTopic, deliveryOptions);
            final VertxAsyncExecutor<AbstractResponse> executor = new VertxAsyncExecutor.Builder(vertx).timeoutMillis(2_000L).build();

            final ExecutionLayer executionLayer = new ServerExecutionLayer(executor, publisher);
            executionLayersForBroadcast.add(executionLayer);

            final ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executionLayer, requestProcessor);
            senderIdToTextMessageHandler.put(senderId, textMessageHandler);

            eventBus.consumer(clientIncomingTopic, clientToServerMessage ->
            {
                String maybeJson = clientToServerMessage.body().toString();
                senderIdToTextMessageHandler.get(senderId).handle(maybeJson);
            });
        });

        waitForServerToStart();
    }

    private static void waitForServerToStart()
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

    public void broadcast(AbstractMessage message)
    {
        Iterator<ExecutionLayer> iterator = executionLayersForBroadcast.iterator();
        while (iterator.hasNext())
        {
            ExecutionLayer next;
            try
            {
                next = iterator.next();
                next.serverSend(message);
            }
            catch (Exception e)
            {
                iterator.remove();
                LOGGER.error("Error writing message", e);
            }
        }
    }
}
