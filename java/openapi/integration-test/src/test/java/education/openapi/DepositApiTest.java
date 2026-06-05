package education.openapi;

import education.common.result.Result;
import education.openapi.api.BalanceApi;
import education.openapi.api.DepositApi;
import education.openapi.api.WithdrawalApi;
import education.openapi.model.Balance;
import education.openapi.application.Application;
import education.openapi.application.handlers.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DepositApiTest
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

        BalanceApi balanceApi = new BalanceApiHandler(balanceCommandHandler);
        DepositApi depositApi = new DepositApiHandler(depositCommandHandler);
        WithdrawalApi withdrawalApi = new WithdrawalApiHandler(withdrawalCommandHandler);

        application = new Application(vertx, balanceApi, depositApi, withdrawalApi);
        application.start(0).toCompletionStage().toCompletableFuture().join();
        port = application.actualPort();
    }

    private void setupSuccessfulMocks()
    {
        given(balanceCommandHandler.handle(anyInt())).willReturn(Result.success(new Balance().balance("0")));
        given(depositCommandHandler.handle(any())).willReturn(Result.success(new Balance().balance("0")));
        given(withdrawalCommandHandler.handle(any())).willReturn(Result.success(new Balance().balance("0")));
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
