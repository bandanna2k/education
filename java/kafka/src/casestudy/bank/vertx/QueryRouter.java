package casestudy.bank.vertx;

import casestudy.bank.projections.AccountDao;
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
    private final AccountDao accountDao;

    public QueryRouter(Vertx vertx, AccountDao accountDao)
    {
        this.vertx = vertx;
        this.accountDao = accountDao;
    }

    public void addRoutes(Router router)
    {
        System.out.println("GET /balances");

        router.route(GET, "/balances").handler(context ->
        {
            vertx.executeBlocking(future ->
            {
                try
                {
                    Balances balances = accountDao.getBalances();
                    context.response().setStatusCode(200).send(OBJECT_MAPPER.writeValueAsString(balances));
                    future.complete();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    future.fail(e.getMessage());
                }
            });
        });
    }
}
