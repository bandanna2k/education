package education.openapi;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationTest {

    private Vertx vertx;
    private WebClient client;
    private Application application;

    @BeforeEach
    void setUp() {
        vertx = createVertx();
        client = WebClient.create(vertx);
        application = new Application(vertx);
        application.start(0).toCompletionStage().toCompletableFuture().join();
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
    }

    @Test
    void withdrawalWithInsufficientFundsReturnsError() {
    }
}
