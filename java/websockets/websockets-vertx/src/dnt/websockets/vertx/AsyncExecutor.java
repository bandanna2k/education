package dnt.websockets.vertx;

import io.vertx.core.*;

public interface AsyncExecutor<Response>
{
    Future<Response> execute(final AsyncRequest asyncRequest);
    void onResponseReceived(final long correlationId, final Response response);
}
