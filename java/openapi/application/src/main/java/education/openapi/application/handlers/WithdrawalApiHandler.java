package education.openapi.application.handlers;

import education.openapi.api.WithdrawalApi;
import education.openapi.api.components.TransactionRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;

import static education.openapi.application.ToBeRenamed.respondError;
import static education.openapi.application.ToBeRenamed.respondJson;

public class WithdrawalApiHandler implements WithdrawalApi
{
    private final WithdrawalCommandHandler commandHandler;

    public WithdrawalApiHandler(WithdrawalCommandHandler commandHandler)
    {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handle(RoutingContext ctx)
    {
        final String accountId = ctx.pathParam("accountId");
//
//        final ValidatedRequest validatedRequest = ctx.get("KEY_META_DATA_VALIDATED_REQUEST");
        final JsonObject jsonBody = ctx.body().asJsonObject();

        final TransactionRequest request = jsonBody.mapTo(TransactionRequest.class);
        final WithdrawalCommand command = new WithdrawalCommand(
                Integer.parseInt(accountId),
                new BigDecimal(request.amount)
        );
        commandHandler.handle(command)
                .consume(
                        balance -> respondJson(ctx, 200, balance),
                        error -> respondError(ctx, error.errorCode, error.name(), error.errorMessage));
    }
}
