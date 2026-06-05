package education.openapi.application.handlers;

import education.openapi.api.BalanceApi;
import io.vertx.ext.web.RoutingContext;

import static education.openapi.application.ToBeRenamed.respondError;
import static education.openapi.application.ToBeRenamed.respondJson;

public class BalanceApiHandler implements BalanceApi
{
    private final BalanceCommandHandler commandHandler;

    public BalanceApiHandler(BalanceCommandHandler commandHandler)
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
