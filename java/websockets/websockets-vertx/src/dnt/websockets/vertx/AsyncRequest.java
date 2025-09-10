package dnt.websockets.vertx;

@FunctionalInterface
public interface AsyncRequest
{
    void invoke(long correlationId);
}
