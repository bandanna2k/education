package education.openapi;

import education.openapi.specfirst.Application;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecFirstTest
{

    private Vertx vertx;
    private WebClient client;
    private Application application;
    private int port;

    @BeforeEach
    void setUp() {
        vertx = createVertx();
        client = WebClient.create(vertx);
        application = new Application(vertx);
        application.start(0).toCompletionStage().toCompletableFuture().join();
        port = application.actualPort();
    }

    private static Vertx createVertx() {
        final Vertx vertx = Vertx.vertx();
        Runtime.getRuntime().addShutdownHook(new Thread(vertx::close));
        return vertx;
    }

    @AfterEach
    void tearDown() {
        application.stop().toCompletionStage().toCompletableFuture().join();
        vertx.close().toCompletionStage().toCompletableFuture().join();
    }

    @Test
    void depositBalanceAndWithdrawalFlow() {
        // Deposit 100.0
        HttpResponse<Buffer> depositResponse = client.post(port, "localhost", "/deposit/{accountId}".replace("{accountId}", "1"))
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("amount", 100.0))
                .toCompletionStage().toCompletableFuture().join();

        assertThat(depositResponse.statusCode()).isEqualTo(200);
        assertThat(depositResponse.bodyAsJsonObject().getString("balance")).isEqualTo("100.0");

        // Check balance
        HttpResponse<Buffer> balanceResponse = client.get(port, "localhost", "/balance")
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("accountId", 1))
                .toCompletionStage().toCompletableFuture().join();

        assertThat(balanceResponse.statusCode()).isEqualTo(200);
        assertThat(balanceResponse.bodyAsJsonObject().getString("balance")).isEqualTo("100.0");

        // Withdraw 40.0
        HttpResponse<Buffer> withdrawalResponse = client.post(port, "localhost", "/withdrawal")
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("accountId", 1).put("amount", 40.0))
                .toCompletionStage().toCompletableFuture().join();

        assertThat(withdrawalResponse.statusCode()).isEqualTo(200);
        assertThat(withdrawalResponse.bodyAsJsonObject().getString("balance")).isEqualTo("60.0");
    }

    @Test
    void withdrawalWithInsufficientFundsReturnsError() {
        // Attempt to withdraw from an account with zero balance
        HttpResponse<Buffer> response = client.post(port, "localhost", "/withdrawal")
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject().put("accountId", 99).put("amount", 50.0))
                .toCompletionStage().toCompletableFuture().join();

        assertThat(response.statusCode()).isEqualTo(400);
        JsonObject body = response.bodyAsJsonObject();
        assertThat(body.getString("code")).isEqualTo("INSUFFICIENT_FUNDS");
        assertThat(body.getString("message")).contains("Insufficient funds");
    }
}
