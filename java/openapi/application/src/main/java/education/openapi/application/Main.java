package education.openapi.application;

import education.openapi.application.handlers.*;
import io.vertx.core.Vertx;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main
{
    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();
        Runtime.getRuntime().addShutdownHook(new Thread(vertx::close));

        Application application = createApplication(vertx);
        application.start(8080)
                .toCompletionStage()
                .toCompletableFuture()
                .join();
        System.out.println("Server started on port " + application.actualPort());
    }

    private static Application createApplication(Vertx vertx)
    {
        final Map<Integer, BigDecimal> balances = new ConcurrentHashMap<>();
        BalanceApiHandler balanceOperation = new BalanceApiHandler(
                new BalanceCommandHandler(balances));
        DepositApiHandler depositOperation = new DepositApiHandler(
                new DepositCommandHandler(balances));
        WithdrawalApiHandler withdrawalOperation = new WithdrawalApiHandler(
                new WithdrawalCommandHandler(balances));

        return new Application(
                vertx,
                balanceOperation,
                depositOperation,
                withdrawalOperation
        );
    }
}