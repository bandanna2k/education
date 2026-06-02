package education.openapi.codefirst.handlers;

import education.openapi.codefirst.operations.BalanceOperation;
import io.vertx.ext.web.RoutingContext;

import static education.openapi.codefirst.operations.ApiOperation.respondError;
import static education.openapi.codefirst.operations.ApiOperation.respondJson;

public class BalanceOperationHandler implements BalanceOperation
{
    private final BalanceCommandHandler commandHandler;

    public BalanceOperationHandler(BalanceCommandHandler commandHandler)
    {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handle(RoutingContext ctx)
    {
        final String accountId = ctx.pathParam("accountId");
        commandHandler.handle(Integer.parseInt(accountId))
                .consume(
                        balance -> respondJson(ctx, 200, balance),
                        error -> respondError(ctx, error.errorCode, error.name(), error.errorMessage));

    }
}
