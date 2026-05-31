package education.openapi.specfirst;

import education.openapi.specfirst.operations.BalanceOperation;
import education.openapi.specfirst.operations.DepositOperation;
import education.openapi.specfirst.operations.WithdrawalOperation;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

    private final Vertx vertx;
    private HttpServer server;

    private final Map<Long, BigDecimal> balances = new ConcurrentHashMap<>();
    private final BalanceOperation balanceOperation = new BalanceOperation(balances);
    private final DepositOperation depositOperation = new DepositOperation(balances);
    private final WithdrawalOperation withdrawalOperation = new WithdrawalOperation(balances);

    public Application(Vertx vertx) {
        this.vertx = vertx;
    }

    public Future<Void> start(int port) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/balance").handler(balanceOperation::handle);
        router.post("/deposit").handler(depositOperation::handle);
        router.post("/withdrawal").handler(withdrawalOperation::handle);

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
}
