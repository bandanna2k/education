package dnt.websockets.client;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.infrastructure.Publisher;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.AbstractRequest;
import dnt.websockets.messages.AbstractResponse;
import dnt.websockets.messages.ErrorResponse;
import dnt.websockets.vertx.VertxAsyncExecutor;
import education.common.result.Result;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientExecutionLayer implements ExecutionLayer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientExecutionLayer.class);

    private final Publisher publisher;
    private final VertxAsyncExecutor<AbstractResponse> executor;

    public ClientExecutionLayer(VertxAsyncExecutor<AbstractResponse> executor, Publisher publisher)
    {
        this.executor = executor;
        this.publisher = publisher;
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> clientRequestFromServer(AbstractRequest request)
    {
        return executor.execute(correlationId -> publisher.send(request.attachCorrelationId(correlationId)))
                .map(ClientExecutionLayer::checkForErrorResponse)
                .recover(throwable ->
                        Future.succeededFuture(Result.failure(throwable.getMessage())))
                .map(result ->
                        result.map(s -> (T)s, Object::toString));
    }

    @Override
    public void serverResponseToRequest(AbstractResponse response)
    {
        executor.onResponseReceived(response.correlationId, response);
    }

    @Override
    public void clientResponseToRequest(AbstractResponse response)
    {
        publisher.send(response);
    }

    @Override
    public void serverSend(AbstractMessage message)
    {
        throw new UnsupportedOperationException("Client will never ask to broadcast a message as a server.");
    }

    @Override
    public void clientSend(AbstractMessage message)
    {
        LOGGER.debug("Client --> Pojo     Server | Sending {}", message);
        publisher.send(message);
    }

    private static Result<AbstractResponse, String> checkForErrorResponse(AbstractResponse data)
    {
        if(data instanceof ErrorResponse errorResponse)
        {
            return Result.failure(errorResponse.message);
        }
        return Result.success(data);
    }
}
