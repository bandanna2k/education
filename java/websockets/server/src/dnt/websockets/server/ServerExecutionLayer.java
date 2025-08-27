package dnt.websockets.server;

import dnt.websockets.communications.*;
import education.common.result.Result;
import io.vertx.core.Future;

public class ServerExecutionLayer implements ExecutionLayer
{
    private final Publisher publisher;

    public ServerExecutionLayer(Publisher publisher)
    {
        this.publisher = publisher;
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request)
    {
        throw new UnsupportedOperationException("Not implemented on server.");
    }

    @Override
    public void respond(AbstractResponse response)
    {
        publisher.send(response);
    }

    @Override
    public void send(AbstractMessage message)
    {
        publisher.send(message);
    }
}
