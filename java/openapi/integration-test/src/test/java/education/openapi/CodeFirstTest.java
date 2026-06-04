package education.openapi;

import education.openapi.common.Application;
import education.openapi.operations.BalanceOperation;
import education.openapi.operations.DepositOperation;
import education.openapi.operations.WithdrawalOperation;
import education.openapi.common.handlers.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class CodeFirstTest
{
    private Vertx vertx;
    private WebClient client;
    private Application application;
    private int port;

    @BeforeEach
    void setUp() {
        vertx = Vertx.vertx();
        client = WebClient.create(vertx);

        final Map<Integer, BigDecimal> balances = new ConcurrentHashMap<>();
        final BalanceOperation balanceOperation = new BalanceOperationHandler(new BalanceCommandHandler(balances));
        final DepositOperation depositOperation = new DepositOperationHandler(new DepositCommandHandler(balances));
        final WithdrawalOperation withdrawalOperation = new WithdrawalOperationHandler(new WithdrawalCommandHandler(balances));

        application = new Application(vertx, balanceOperation, depositOperation, withdrawalOperation);
        application.start(0).toCompletionStage().toCompletableFuture().join();
        port = application.actualPort();
    }

    @AfterEach
    void tearDown() {
        application.stop().toCompletionStage().toCompletableFuture().join();
        vertx.close().toCompletionStage().toCompletableFuture().join();
    }

    @Test
    public void depositBalanceAndWithdrawalFlow() {

        HttpResponse<Buffer> depositResponse = client.post(port, "localhost", "/deposit/{accountId}".replace("{accountId}", "1"))
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("amount", 100.0))
                .toCompletionStage().toCompletableFuture().join();
        assertThat(depositResponse.statusCode()).isEqualTo(200);

        HttpResponse<Buffer> balanceResponse = client.get(port, "localhost", "/balance/{accountId}".replace("{accountId}", "1"))
                .putHeader("Content-Type", "application/json")
                .send()
                .toCompletionStage().toCompletableFuture().join();
        assertThat(balanceResponse.statusCode()).isEqualTo(200);

        HttpResponse<Buffer> withdrawalResponse = client.post(port, "localhost", "/withdrawal/{accountId}".replace("{accountId}", "1"))
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("amount", 40.0))
                .toCompletionStage().toCompletableFuture().join();
        assertThat(withdrawalResponse.statusCode()).isEqualTo(200);
    }

    @Test
    public void withdrawalWithInsufficientFundsReturnsError() {
        // Attempt to withdraw from an account with zero balance
        HttpResponse<Buffer> response = client.post(port, "localhost", "/withdrawal/{accountId}".replace("{accountId}", "99"))
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("amount", 50.0))
                .toCompletionStage().toCompletableFuture().join();

        assertThat(response.statusCode()).isEqualTo(400);
        JsonObject body = response.bodyAsJsonObject();
        assertThat(body.getString("code")).isEqualTo("INSUFFICIENT_FUNDS");
        assertThat(body.getString("message")).contains("Insufficient funds");
    }
}
