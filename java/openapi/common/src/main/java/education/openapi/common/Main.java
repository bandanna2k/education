package education.openapi.common;

import education.openapi.codefirst.generator.SpecGenerator;
import education.openapi.codefirst.handlers.*;
import education.openapi.common.handlers.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.vertx.core.Vertx;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main
{
    public static void main(String[] args)
    {
        OpenAPI openApi = new SpecGenerator().generate();

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
        BalanceOperationHandler balanceOperation = new BalanceOperationHandler(
                new BalanceCommandHandler(balances));
        DepositOperationHandler depositOperation = new DepositOperationHandler(
                new DepositCommandHandler(balances));
        WithdrawalOperationHandler withdrawalOperation = new WithdrawalOperationHandler(
                new WithdrawalCommandHandler(balances));

        return new Application(
                vertx,
                balanceOperation,
                depositOperation,
                withdrawalOperation
        );
    }
}