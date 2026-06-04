package education.openapi.common.handlers;

import education.openapi.operations.WithdrawalOperation;
import education.openapi.operations.components.TransactionRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;

import static education.openapi.operations.ApiOperation.respondError;
import static education.openapi.operations.ApiOperation.respondJson;

public class WithdrawalOperationHandler implements WithdrawalOperation
{
    private final WithdrawalCommandHandler commandHandler;

    public WithdrawalOperationHandler(WithdrawalCommandHandler commandHandler)
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
