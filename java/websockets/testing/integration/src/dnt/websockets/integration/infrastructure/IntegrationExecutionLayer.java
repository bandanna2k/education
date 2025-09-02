package dnt.websockets.integration.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.integration.MessageCollector;
import dnt.websockets.messages.*;
import dnt.websockets.server.ServerTextMessageHandler;
import education.common.result.Result;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

public class IntegrationExecutionLayer implements ExecutionLayer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationExecutionLayer.class);

    private final ServerTextMessageHandler serverTextMessageHandler;
    private final Map<String, ClientTextMessageHandler> clientTextMessageHandlers = new HashMap<>();
    private final MessageCollector internalClientMessageCollector;
    private final MessageCollector internalServerMessageCollector;

    private Optional<String> maybeFailNextMessage = Optional.empty();
    private boolean throwOnNextMessage = false;

    private final Queue<DeferredFuture<?>> deferredFutures = new LinkedList<>();
    private boolean pauseProcessing;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // Only use for rewriting a request

    public IntegrationExecutionLayer(MessageVisitor serverMessageProcessor,
                                     MessageVisitor clientMessageProcessor)
    {
        this.internalClientMessageCollector = new MessageCollector("Internal Client", clientMessageProcessor);
        this.internalServerMessageCollector = new MessageCollector("Internal Server", serverMessageProcessor);

        this.serverTextMessageHandler = new ServerTextMessageHandler(this, serverMessageProcessor);
    }

    public void register(String clientId, ClientTextMessageHandler clientTextMessageHandler)
    {
        clientTextMessageHandlers.put(clientId, clientTextMessageHandler);
    }

    @Override
    public void serverCompleteResponse(AbstractResponse response)
    {
        response.visit(this, internalClientMessageCollector);
    }

    @Override
    public void clientCompleteResponse(AbstractResponse response)
    {
        response.visit(this, internalServerMessageCollector);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> serverRequestOnClient(AbstractServerRequest request)
    {
        final ClientTextMessageHandler clientTextMessageHandler = clientTextMessageHandlers.get(request.clientId);
        final Supplier<Result<T, Object>> requestProcessor = () ->
        {
            try
            {
                String serialisedRequest = OBJECT_MAPPER.writeValueAsString(request);
                clientTextMessageHandler.handle(serialisedRequest);
                T lastMessage = internalServerMessageCollector.getLastMessage();
                if (lastMessage == null)
                {
                    LOGGER.error("Client  X- JSON <-- Server | No client response received {}", request);
                    return Result.failure("No response received");
                }
                assert canJacksonDeserialize(ServerTextMessageHandler.OBJECT_MAPPER, lastMessage)
                        : String.format("ServerTextMessageHandler Cannot serialise %s", lastMessage.getClass().getSimpleName());
                return intercept(request, lastMessage);
            }
            catch (JsonProcessingException e)
            {
                throw new RuntimeException(e);
            }
        };
        return request(requestProcessor);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> clientRequestFromServer(AbstractRequest request)
    {
        final Supplier<Result<T, Object>> processRequest = () ->
        {
            try
            {
                String serialisedRequest = OBJECT_MAPPER.writeValueAsString(request);
                serverTextMessageHandler.handle(serialisedRequest);
                T lastMessage = internalClientMessageCollector.getLastMessage();
                if (lastMessage == null)
                {
                    LOGGER.error("Client --> JSON -X  Server | No server response received {}", request);
                    return Result.failure("No response received");
                }
                assert canJacksonDeserialize(ClientTextMessageHandler.OBJECT_MAPPER, lastMessage)
                        : String.format("ClientTextMessageHandler Cannot serialise %s", lastMessage.getClass().getSimpleName());
                return intercept(request, lastMessage);
            }
            catch (JsonProcessingException e)
            {
                throw new RuntimeException(e);
            }
        };
        return request(processRequest);
    }

    private <T extends AbstractResponse> Future<Result<T, String>> request(Supplier<Result<T, Object>> processRequest)
    {
        if(pauseProcessing)
        {
            DeferredFuture<Result<T, Object>> deferredFuture = new DeferredFuture<>(processRequest);
            deferredFutures.add(deferredFuture);
            return deferredFuture.future().map(r -> r.mapError(String::valueOf));
        }
        return Future.succeededFuture()
                .map(unused -> processRequest.get())
                .map(r -> r.mapError(String::valueOf));
    }

    private <T extends AbstractResponse> Result<T, Object> intercept(AbstractRequest notNeedForAnyScenarioYet, T lastMessage)
    {
        if(throwOnNextMessage)
        {
            throwOnNextMessage = false;
            throw new RuntimeException("Throw on next message");
        }
        if(maybeFailNextMessage.isPresent())
        {
            Result<T, Object> failure = Result.failure(maybeFailNextMessage.get());
            maybeFailNextMessage = Optional.empty();
            return failure;
        }
        if(lastMessage instanceof ErrorResponse errorResponse) // Nice addition JDK 21
        {
            return Result.failure(errorResponse.message);
        }
        return Result.success(lastMessage);
    }

    @Override
    public void serverSend(AbstractMessage message)
    {
        try
        {
            final String json = OBJECT_MAPPER.writeValueAsString(message);
            clientTextMessageHandlers.values().forEach(clientTextMessageHandler ->
                    clientTextMessageHandler.handle(json));
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clientSend(AbstractMessage message)
    {
        try
        {
            serverTextMessageHandler.handle(ClientTextMessageHandler.OBJECT_MAPPER.writeValueAsString(message)); // Prove our serde works.
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean canJacksonDeserialize(ObjectMapper mapper, AbstractMessage message)
    {
        try
        {
            String serialised = OBJECT_MAPPER.writeValueAsString(message);
            mapper.readValue(serialised, AbstractMessage.class);
            return true;

        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void failNextMessage(String failureMessage)
    {
        maybeFailNextMessage = Optional.of(failureMessage);
    }

    public void throwOnNextMessage()
    {
        throwOnNextMessage = true;
    }

    public void pauseProcessing()
    {
        this.pauseProcessing = true;
    }
    public void resumeProcessing(int messageCount)
    {
        this.pauseProcessing = false;
        int count = Math.min(deferredFutures.size(), messageCount);
        for (int i = 0; i < count; i++)
        {
            deferredFutures.remove().complete();
        }
    }
    public boolean isComplete()
    {
        return deferredFutures.isEmpty();
    }
}
