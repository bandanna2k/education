package dnt.websockets.vertx;

import io.vertx.core.Future;

public interface AsyncExecutor<Response>
{
    Future<Response> execute(final AsyncRequest asyncRequest);

    void onResponseReceived(long correlationId, Response response);
}
