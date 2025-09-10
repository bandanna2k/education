package dnt.websockets.infrastructure;

import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.AbstractRequest;
import dnt.websockets.messages.AbstractResponse;
import education.common.result.Result;
import io.vertx.core.Future;

interface ClientExecutionLayer
{
    default <T extends AbstractResponse> Future<Result<T, String>> clientRequestFromServer(AbstractRequest request)
    {
        throw new UnsupportedOperationException();
    }
    default void clientCompleteResponse(AbstractResponse response)
    {
        throw new UnsupportedOperationException();
    }
    void clientSend(AbstractMessage message);
}
