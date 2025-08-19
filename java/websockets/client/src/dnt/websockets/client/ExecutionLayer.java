package dnt.websockets.client;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.AbstractRequest;
import dnt.websockets.communications.AbstractResponse;
import education.common.result.Result;
import io.vertx.core.Future;

public interface ExecutionLayer
{
    <T extends AbstractResponse> Future<Result<T, String>> sendClientToServer(AbstractRequest request);

    void broadcastServerToClient(AbstractMessage pushMessage);
}
