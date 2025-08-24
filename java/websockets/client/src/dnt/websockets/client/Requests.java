package dnt.websockets.client;

import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyResponse;
import education.common.result.Result;
import io.vertx.core.Future;

public interface Requests
{
    Future<Result<GetPropertyResponse, String>> getProperty(String key);

    Future<Result<SetPropertyResponse, String>> setProperty(String key, String value);
}
