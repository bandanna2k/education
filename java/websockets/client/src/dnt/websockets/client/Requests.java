package dnt.websockets.client;

import dnt.websockets.communications.OptionsResponse;
import education.common.result.Result;
import io.vertx.core.Future;

public interface Requests
{
    Future<Result<OptionsResponse, String>> requestOptions();
}
