package education.openapi.codefirst;

import com.fasterxml.jackson.databind.ObjectMapper;
import education.openapi.codefirst.components.AccountRequest;
import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.ErrorResponse;
import education.openapi.codefirst.components.TransactionRequest;
import io.swagger.client.api.BalanceApi;
import io.swagger.client.api.DepositApi;
import io.swagger.client.api.WithdrawalApi;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Application implements BalanceApi, DepositApi, WithdrawalApi {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Vertx vertx;
    private final Map<Long, BigDecimal> balances = new ConcurrentHashMap<>();
    private HttpServer server;

    public Application(Vertx vertx) {
        this.vertx = vertx;
    }

    public Future<Void> start(int port) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/balance").handler(this::handleBalance);
        router.post("/deposit").handler(this::handleDeposit);
        router.post("/withdrawal").handler(this::handleWithdrawal);

        Promise<Void> promise = Promise.promise();
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port)
                .onSuccess(httpServer -> {
                    this.server = httpServer;
                    promise.complete();
                })
                .onFailure(promise::fail);
        return promise.future();
    }

    public Future<Void> stop() {
        if (server == null) return Future.succeededFuture();
        return server.close().mapEmpty();
    }

    public int actualPort() {
        if (server == null) throw new IllegalStateException("Server not started");
        return server.actualPort();
    }

    // --- BalanceApi ---

    @Override
    public Balance getBalance(AccountRequest accountRequest) {
        BigDecimal amount = balances.getOrDefault(accountRequest.getAccountId(), BigDecimal.ZERO);
        return new Balance().balance(amount.toPlainString());
    }

    // --- DepositApi ---

    @Override
    public Balance postDeposit(TransactionRequest transactionRequest) {
        BigDecimal amount = BigDecimal.valueOf(transactionRequest.getAmount());
        BigDecimal newBalance = balances.merge(
                transactionRequest.getAccountId(), amount, BigDecimal::add);
        return new Balance().balance(newBalance.toPlainString());
    }

    // --- WithdrawalApi ---

    @Override
    public Balance postWithdrawal(TransactionRequest transactionRequest) {
        BigDecimal amount = BigDecimal.valueOf(transactionRequest.getAmount());
        long accountId = transactionRequest.getAccountId();
        BigDecimal current = balances.getOrDefault(accountId, BigDecimal.ZERO);
        if (current.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds: balance is " + current.toPlainString());
        }
        BigDecimal newBalance = balances.merge(accountId, amount.negate(), BigDecimal::add);
        return new Balance().balance(newBalance.toPlainString());
    }

    // --- Vert.x route handlers ---

    private void handleBalance(RoutingContext ctx) {
        try {
            AccountRequest req = MAPPER.readValue(ctx.body().asString(), AccountRequest.class);
            Balance result = getBalance(req);
            respondJson(ctx, 200, result);
        } catch (Exception e) {
            respondError(ctx, 400, "BAD_REQUEST", e.getMessage());
        }
    }

    private void handleDeposit(RoutingContext ctx) {
        try {
            TransactionRequest req = MAPPER.readValue(ctx.body().asString(), TransactionRequest.class);
            Balance result = postDeposit(req);
            respondJson(ctx, 200, result);
        } catch (Exception e) {
            respondError(ctx, 400, "BAD_REQUEST", e.getMessage());
        }
    }

    private void handleWithdrawal(RoutingContext ctx) {
        try {
            TransactionRequest req = MAPPER.readValue(ctx.body().asString(), TransactionRequest.class);
            Balance result = postWithdrawal(req);
            respondJson(ctx, 200, result);
        } catch (InsufficientFundsException e) {
            respondError(ctx, 400, "INSUFFICIENT_FUNDS", e.getMessage());
        } catch (Exception e) {
            respondError(ctx, 400, "BAD_REQUEST", e.getMessage());
        }
    }

    private void respondJson(RoutingContext ctx, int status, Object body) {
        try {
            String json = MAPPER.writeValueAsString(body);
            ctx.response()
                    .setStatusCode(status)
                    .putHeader("Content-Type", "application/json")
                    .end(json);
        } catch (Exception e) {
            ctx.response().setStatusCode(500).end("Internal error");
        }
    }

    private void respondError(RoutingContext ctx, int status, String code, String message) {
        ErrorResponse error = new ErrorResponse().code(code).message(message);
        respondJson(ctx, status, error);
    }

    static class InsufficientFundsException extends RuntimeException {
        InsufficientFundsException(String message) {
            super(message);
        }
    }
}
