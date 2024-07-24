package casestudy.bank.publishers;

import education.jackson.requests.Request;
import education.jackson.response.Response;
import io.vertx.core.Vertx;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestExecutor
{
    private final Vertx vertx;
    private final RequestPublisher publisher;
    private final Map<UUID, Request> requests = new HashMap<>();
    private final Map<UUID, Long> timers = new HashMap<>();

    public RequestExecutor(final Vertx vertx, final RequestPublisher publisher)
    {
        this.vertx = vertx;
        this.publisher = publisher;
    }

    public void request(final Request request)
    {
        final long timerId = vertx.setTimer(5000, l ->
        {
            System.out.println("Request timed out: " + request);
            requests.remove(request.uuid);
        });
        requests.put(request.uuid, request);
        timers.put(request.uuid, timerId);

        publisher.publishRequest(request);
    }

    public void onResponse(final Response response)
    {
        final Request request = requests.remove(response.uuid);
        if(request == null)
        {
            System.out.println("Request not found for response: " + response);
        }
        else
        {
            final long timerId = timers.remove(response.uuid);
            vertx.cancelTimer(timerId);

            System.out.printf("Request found for response: Request: %s, Response: %s%n", request, response);
        }
    }
}
