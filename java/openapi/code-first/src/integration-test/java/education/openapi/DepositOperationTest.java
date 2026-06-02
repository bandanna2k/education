package education.openapi;

import education.common.result.Result;
import education.openapi.codefirst.Application;
import education.openapi.codefirst.handlers.*;
import education.openapi.codefirst.operations.BalanceOperation;
import education.openapi.codefirst.operations.DepositOperation;
import education.openapi.codefirst.operations.WithdrawalOperation;
import education.openapi.codefirst.operations.components.Balance;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DepositOperationTest
{
    private Vertx vertx;
    private WebClient client;
    private Application application;
    private int port;

    private final BalanceCommandHandler balanceCommandHandler = mock(BalanceCommandHandler.class);
    private final DepositCommandHandler depositCommandHandler = mock(DepositCommandHandler.class);
    private final WithdrawalCommandHandler withdrawalCommandHandler = mock(WithdrawalCommandHandler.class);

    @BeforeEach
    void setUp() {
        vertx = Vertx.vertx();
        client = WebClient.create(vertx);

        BalanceOperation balanceOperation = new BalanceOperationHandler(balanceCommandHandler);
        DepositOperation depositOperation = new DepositOperationHandler(depositCommandHandler);
        WithdrawalOperation withdrawalOperation = new WithdrawalOperationHandler(withdrawalCommandHandler);

        application = new Application(vertx, balanceOperation, depositOperation, withdrawalOperation);
        application.start(0).toCompletionStage().toCompletableFuture().join();
        port = application.actualPort();
    }

    private void setupSuccessfulMocks()
    {
        given(balanceCommandHandler.handle(anyInt())).willReturn(Result.success(new Balance("0")));
        given(depositCommandHandler.handle(any())).willReturn(Result.success(new Balance("0")));
        given(withdrawalCommandHandler.handle(any())).willReturn(Result.success(new Balance("0")));
    }

    @AfterEach
    void tearDown() {
        application.stop().toCompletionStage().toCompletableFuture().join();
        vertx.close().toCompletionStage().toCompletableFuture().join();
    }

    @Test
    public void shouldDeposit()
    {
        setupSuccessfulMocks();

        HttpResponse<Buffer> depositResponse = client.post(port, "localhost", "/deposit/{accountId}".replace("{accountId}", "1"))
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("amount", 100.0))
                .toCompletionStage().toCompletableFuture().join();
        assertThat(depositResponse.statusCode()).isEqualTo(200);
    }

    @Test
    public void shouldFailWithBadAccountId()
    {
        setupSuccessfulMocks();

        String accountId = "XXX";
        HttpResponse<Buffer> depositResponse = client.post(port, "localhost", "/deposit/{accountId}".replace("{accountId}", accountId))
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("amount", 100.0))
                .toCompletionStage().toCompletableFuture().join();
        assertThat(depositResponse.statusCode())
                .describedAs(depositResponse.bodyAsString())
                .isEqualTo(404);
        assertThat(depositResponse.bodyAsJsonObject().getString("message"))
                .describedAs(depositResponse.bodyAsString())
                .contains("Invalid path parameter. XXX");
    }

    @Test
    public void shouldFailWithBadAmount()
    {
        setupSuccessfulMocks();

        String accountId = "1";
        HttpResponse<Buffer> depositResponse = client.post(port, "localhost", "/deposit/{accountId}".replace("{accountId}", accountId))
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("amount", "XXX"))
                .toCompletionStage().toCompletableFuture().join();
        assertThat(depositResponse.statusCode())
                .describedAs(depositResponse.bodyAsString())
                .isEqualTo(404);
        assertThat(depositResponse.bodyAsJsonObject().getString("message"))
                .describedAs(depositResponse.bodyAsString())
                .contains("Character X is neither a decimal digit number");
    }
}
