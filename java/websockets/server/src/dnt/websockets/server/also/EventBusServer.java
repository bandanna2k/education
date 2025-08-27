package dnt.websockets.server.also;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.Publisher;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import dnt.websockets.server.ServerExecutionLayer;
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

    private final List<ServerTextMessageHandler> textMessageHandlers = new ArrayList<>();
    private final Vertx vertx;
    private final RequestProcessor requestProcessor = new RequestProcessor();
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
                .addInboundPermitted(new PermittedOptions().setAddress("client.request"))
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("client\\..*"));

        TcpEventBusBridge.create(vertx, options)
                .listen(7779, result -> {
                    if (result.succeeded())
                    {
                        LOGGER.info("TCP Event Bus Bridge listening.");
                    }
                    else
                    {
                        throw new RuntimeException("Failed to start bridge: " + result.cause());
                    }
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
            String clientTopic = "client." + senderId;
            registeredClients.put(senderId, clientTopic);

            LOGGER.info("Client registered: " + senderId + " -> " + clientTopic);

            eventBus.send(clientTopic, "Welcome. Thank you.");
            message.reply("Registered. Thank you.");

            DeliveryOptions deliveryOptions = new DeliveryOptions().addHeader("senderId", senderId);
            Publisher publisher = new EventBusPublisher(eventBus, clientTopic, deliveryOptions);
            ExecutionLayer executionLayer = new ServerExecutionLayer(publisher);
            ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executionLayer, requestProcessor);
            textMessageHandlers.add(textMessageHandler);
            senderIdToTextMessageHandler.put(senderId, textMessageHandler);
        });

        eventBus.consumer("client.request", message ->
        {
            String senderId = message.headers().get("senderId");
            String maybeJson = message.body().toString();
            System.out.println(senderId);
            System.out.println(maybeJson);
            senderIdToTextMessageHandler.get(senderId).handle(maybeJson);
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
        Iterator<ServerTextMessageHandler> iterator = textMessageHandlers.iterator();
        while (iterator.hasNext())
        {
            ServerTextMessageHandler next;
            try
            {
                next = iterator.next();
                next.send(message);
            }
            catch (Exception e)
            {
                iterator.remove();
                LOGGER.error("Error writing message", e);
            }
        }
    }
}
