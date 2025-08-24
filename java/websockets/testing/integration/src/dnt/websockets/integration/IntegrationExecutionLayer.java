package dnt.websockets.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.RequestProcessor;
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
    private final PushMessageCollector collector;

    private Optional<String> maybeFailNextMessage = Optional.empty();
    private boolean throwOnNextMessage = false;

    private final Queue<DeferredFuture<?>> deferredFutures = new LinkedList<>();
    private boolean pauseProcessing;

    public IntegrationExecutionLayer(RequestProcessor requestProcessor, PushMessageCollector collector)
    {
        this.publisher = new IntegrationPublisher(collector);
        this.collector = collector;

        this.serverTextMessageHandler = new ServerTextMessageHandler(this, requestProcessor);
        this.clientTextMessageHandler = new ClientTextMessageHandler(this, this.collector);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request)
    {
        final Supplier<Result<T, Object>> processRequest = () ->
        {
            try
            {
                serverTextMessageHandler.handle(ServerTextMessageHandler.OBJECT_MAPPER.writeValueAsString(request));
                T lastMessage = collector.getLastMessage();
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
    public void respond(AbstractResponse response)
    {
        publisher.send(response);
    }

    @Override
    public void send(AbstractMessage message)
    {
        try
        {
            clientTextMessageHandler.handle(ClientTextMessageHandler.OBJECT_MAPPER.writeValueAsString(message)); // Prove our serde works.
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
