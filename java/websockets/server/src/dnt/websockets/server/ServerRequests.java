package dnt.websockets.server;

import dnt.websockets.messages.GetStatusResponse;
import education.common.result.Result;
import io.vertx.core.Future;

public interface ServerRequests
{
    Future<Result<GetStatusResponse, String>> getStatus(String clientId);
}
