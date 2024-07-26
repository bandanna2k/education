package casestudy.bank.vertx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import education.jackson.response.Balance;
import education.jackson.response.Balances;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.util.List;
import java.util.UUID;

import static io.vertx.core.http.HttpMethod.GET;

public class QueryRouter
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final Vertx vertx;

    public QueryRouter(Vertx vertx)
    {
        this.vertx = vertx;
    }

    public void addRoutes(Router router)
    {
        router.route(GET, "/balances").handler(context ->
        {
            vertx.executeBlocking(future ->
            {
                try
                {
                    Balances balances = getBalances();
                    context.response().setStatusCode(200).send(OBJECT_MAPPER.writeValueAsString(balances));
                    future.complete();
                }
                catch (JsonProcessingException e)
                {
                    future.fail(e.getMessage());
                }
            });
        });
    }

    private Balances getBalances()
    {
        return new Balances(UUID.randomUUID(), List.of(
                new Balance(null, 1L, "1000"),
                new Balance(null, 2L, "2000"),
                new Balance(null, 3L, "3000")
        ));
    }
}
