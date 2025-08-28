package dnt.websockets.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.ServerMessageProcessor;
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

    private final IntegrationPublisher publisher;

    private final ServerTextMessageHandler serverTextMessageHandler;
    private final ClientTextMessageHandler clientTextMessageHandler;
    private final MessageCollector clientMessageCollector = new MessageCollector();
    private final MessageCollector serverMessageCollector = new MessageCollector();

    private Optional<String> maybeFailNextMessage = Optional.empty();
    private boolean throwOnNextMessage = false;

    private final Queue<DeferredFuture<?>> deferredFutures = new LinkedList<>();
    private boolean pauseProcessing;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // Only use for rewriting a request

    public IntegrationExecutionLayer(ServerMessageProcessor serverMessageProcessor, ClientMessageProcessor clientMessageProcessor)
    {
        this.publisher = new IntegrationPublisher(this, clientMessageProcessor);

        this.serverTextMessageHandler = new ServerTextMessageHandler(this, serverMessageProcessor);
        this.clientTextMessageHandler = new ClientTextMessageHandler(this, clientMessageProcessor);
    }

    @Override
    public void serverResponseToRequest(AbstractResponse response)
    {
        publisher.send(response);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> serverRequestFromClient(AbstractRequest request)
    {
        final Supplier<Result<T, Object>> processRequest = () ->
        {
            try
            {
                String serialisedRequest = OBJECT_MAPPER.writeValueAsString(request);
                clientTextMessageHandler.handle(serialisedRequest);
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
        return request(request, processRequest);
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
        return request(request, processRequest);
    }

    private <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request, Supplier<Result<T, Object>> processRequest)
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
            clientTextMessageHandler.handle(ServerTextMessageHandler.OBJECT_MAPPER.writeValueAsString(message)); // Prove our serde works.
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
