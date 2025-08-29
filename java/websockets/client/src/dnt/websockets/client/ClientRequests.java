package dnt.websockets.client;

import dnt.websockets.messages.GetPropertyResponse;
import dnt.websockets.messages.SetPropertyResponse;
import education.common.result.Result;
import io.vertx.core.Future;

public interface ClientRequests
{
    Future<Result<GetPropertyResponse, String>> getProperty(String key);

    Future<Result<SetPropertyResponse, String>> setProperty(String key, String value);
}
