package education.openapi.common.handlers;

import education.openapi.operations.ApiOperation;
import education.openapi.operations.BalanceOperation;
import io.vertx.ext.web.RoutingContext;

import static education.openapi.operations.ApiOperation.respondError;
import static education.openapi.operations.ApiOperation.respondJson;

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
