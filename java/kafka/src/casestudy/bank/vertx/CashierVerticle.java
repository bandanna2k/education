package casestudy.bank.vertx;

import casestudy.bank.projections.AccountDao;
import casestudy.bank.publishers.AsyncExecutor;
import casestudy.bank.publishers.RequestPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import education.jackson.requests.Deposit;
import education.jackson.requests.Withdrawal;
import education.jackson.response.Response;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.UUID;

import static io.vertx.core.http.HttpMethod.POST;

public class CashierVerticle extends AbstractVerticle
{
    private static final int PORT = 8888;

    private final RequestPublisher publisher;
    private final AsyncExecutor executor;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final QueryRouter queryRouter;

    public CashierVerticle(final Vertx vertx,
                           final RequestPublisher publisher,
                           final AsyncExecutor executor,
                           final AccountDao accountDao)
    {
        this.publisher = publisher;
        this.executor = executor;
        this.vertx = vertx;
        this.queryRouter = new QueryRouter(vertx, accountDao);
    }

    @Override
    public void start(final Promise<Void> startPromise)
    {
        final Router router = Router.router(vertx);
        router.route(POST, "/deposit").handler(this::depositHandler);
        router.route(POST, "/withdraw").handler(this::withdrawalHandler);

        queryRouter.addRoutes(router);

        vertx.createHttpServer().requestHandler(router).listen(PORT, http ->
        {
            if (http.succeeded())
            {
                startPromise.complete();
                System.out.println("Vertx started on " + PORT);
            }
            else
            {
                startPromise.fail(http.cause());
            }
        });
    }

    private void depositHandler(RoutingContext event)
    {
        final long accountId = Long.parseLong(event.queryParam("accountId").getFirst());
        final BigDecimal amount = new BigDecimal(event.queryParam("amount").getFirst());

        final Deposit deposit = new Deposit(UUID.randomUUID(), accountId, amount);

        final Future<Response> response = deposit(deposit)
                .onSuccess(success -> returnSuccess(event, success))
                .onFailure(error -> returnError(event, error));
    }

    private void withdrawalHandler(RoutingContext event)
    {
        final long accountId = Long.parseLong(event.queryParam("accountId").getFirst());
        final BigDecimal amount = new BigDecimal(event.queryParam("amount").getFirst());

        final Withdrawal withdrawal = new Withdrawal(UUID.randomUUID(), accountId, amount);

        final Future<Response> response = withdrawal(withdrawal)
                .onSuccess(success -> returnSuccess(event, success))
                .onFailure(error -> returnError(event, error));
    }

    private void returnError(final RoutingContext event, final Throwable error)
    {
        System.out.println("Response: " + error);
        try
        {
            event.response().setStatusCode(200);
            event.response().send(objectMapper.writeValueAsString(error.getMessage()));
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void returnSuccess(final RoutingContext event, final Response success)
    {
        System.out.println("Response: " + success);
        try
        {
            event.response().setStatusCode(200);
            event.response().send(objectMapper.writeValueAsString(success));
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Future<Response> deposit(Deposit deposit)
    {
        return executor.execute(deposit.uuid, uuid -> publisher.publishRequest(deposit));
    }

    private Future<Response> withdrawal(Withdrawal withdrawal)
    {
        return executor.execute(withdrawal.uuid, uuid -> publisher.publishRequest(withdrawal));
    }
}
