package dnt.websockets.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.ServerTextMessageHandler;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

public class IntegrationExecutionLayer implements ExecutionLayer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationExecutionLayer.class);

    private final IntegrationPublisher toServerPublisher;
    private final IntegrationPublisher toClientPublisher;

    private final ServerTextMessageHandler serverTextMessageHandler;
    private final MessageCollector clientMessageCollector;
    private final MessageCollector serverMessageCollector;

    private Optional<String> maybeFailNextMessage = Optional.empty();
    private boolean throwOnNextMessage = false;

    private final Queue<DeferredFuture<?>> deferredFutures = new LinkedList<>();
    private boolean pauseProcessing;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // Only use for rewriting a request

    public IntegrationExecutionLayer(MessageVisitor serverMessageProcessor,
                                     MessageVisitor clientMessageProcessor)
    {
        this.clientMessageCollector = new MessageCollector("Internal Client", clientMessageProcessor);
        this.serverMessageCollector = new MessageCollector("Internal Server", serverMessageProcessor);

        this.toClientPublisher = new IntegrationPublisher(this, clientMessageCollector);
        this.toServerPublisher = new IntegrationPublisher(this, serverMessageCollector);

        this.serverTextMessageHandler = new ServerTextMessageHandler(this, serverMessageProcessor);
    }

    @Override
    public void serverResponseToRequest(AbstractResponse response)
    {
        toClientPublisher.send(response);
    }

    @Override
    public void clientResponseToRequest(AbstractResponse response)
    {
        response.visit(this, serverMessageCollector);
    }

    public Map<String, ClientTextMessageHandler> clients = new HashMap<>();
    public void register(String clientId, ClientTextMessageHandler clientTextMessageHandler)
    {
        clients.put(clientId, clientTextMessageHandler);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> serverRequestOnClient(AbstractServerRequest request)
    {
        final ClientTextMessageHandler messageHandler = clients.get(request.clientId);
        final Supplier<Result<T, Object>> processRequest = () ->
        {
            try
            {
                String serialisedRequest = OBJECT_MAPPER.writeValueAsString(request);
                messageHandler.handle(serialisedRequest);
                T lastMessage = serverMessageCollector.getLastMessage();
                if (lastMessage == null)
                {
                    LOGGER.error("No response received.");
                    return Result.failure("No response received");
                }
                return intercept(request, lastMessage);
            }
            catch (JsonProcessingException e)
            {
                throw new RuntimeException(e);
            }
        };
        return request(processRequest);
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
                T lastMessage = clientMessageCollector.getLastMessage();
                if (lastMessage == null)
                {
                    LOGGER.error("No response received.");
                    return Result.failure("No response received");
                }
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
            clients.values().forEach(clientTextMessageHandler ->
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


    private static class DeferredFuture<T>
    {
        private final Promise<T> promise;
        private final Supplier<T> supplier;

        public DeferredFuture(Supplier<T> supplier)
        {
            this.promise = Promise.promise();
            this.supplier = supplier;
        }

        public Future<T> future()
        {
            return promise.future();
        }

        public void complete()
        {
            try
            {
                T result = supplier.get();
                promise.complete(result);
            }
            catch (Exception e)
            {
                promise.fail(e);
            }
        }
    }
}
