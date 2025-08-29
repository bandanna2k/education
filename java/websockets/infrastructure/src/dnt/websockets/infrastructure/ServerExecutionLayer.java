package dnt.websockets.infrastructure;

import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.AbstractResponse;
import dnt.websockets.messages.AbstractServerRequest;
import education.common.result.Result;
import io.vertx.core.Future;

interface ServerExecutionLayer
{
    default <T extends AbstractResponse> Future<Result<T, String>> serverRequestOnClient(AbstractServerRequest request)
    {
        throw new UnsupportedOperationException();
    }
    default void serverResponseToRequest(AbstractResponse response)
    {
        throw new UnsupportedOperationException();
    }
    void serverSend(AbstractMessage message);
}
