package dnt.websockets.server.vertx;

import dnt.websockets.communications.*;
import education.common.result.Result;
import io.vertx.core.Future;

public class VertxServerExecutionLayer implements ExecutionLayer
{
    private final Publisher messagePublisher;

    public VertxServerExecutionLayer(Publisher publisher)
    {
        this.messagePublisher = publisher;
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request)
    {
        throw new UnsupportedOperationException("Not implemented on server.");
    }

    @Override
    public void respond(AbstractResponse response)
    {
        messagePublisher.send(response);
    }

    @Override
    public void send(AbstractMessage message)
    {
        messagePublisher.send(message);
    }
}
