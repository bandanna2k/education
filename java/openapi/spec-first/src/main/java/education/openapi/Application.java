package education.openapi;

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

public class Application {
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
        if (server == null) {
            return Future.succeededFuture();
        }
        return server.close().mapEmpty();
    }

    public int actualPort() {
        if (server == null) {
            throw new IllegalStateException("Server not started");
        }
        return server.actualPort();
    }

    private void handleBalance(RoutingContext context) {
    }

    private void handleDeposit(RoutingContext context) {
    }

    private void handleWithdrawal(RoutingContext context) {
    }
}
