package education.openapi.common.handlers;

import education.openapi.operations.DepositOperation;
import education.openapi.operations.components.TransactionRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;

import static education.openapi.operations.ApiOperation.*;
import static education.openapi.operations.ApiOperation.respondError;
import static education.openapi.operations.ApiOperation.respondJson;

public class DepositOperationHandler implements DepositOperation
{
    private final DepositCommandHandler commandHandler;

    public DepositOperationHandler(DepositCommandHandler commandHandler)
    {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handle(RoutingContext ctx)
    {
        final int accountId = tryParseInt(ctx.pathParam("accountId"));
//
//        final ValidatedRequest validatedRequest = ctx.get("KEY_META_DATA_VALIDATED_REQUEST");
        final JsonObject jsonBody = ctx.body().asJsonObject();

        final TransactionRequest request = jsonBody.mapTo(TransactionRequest.class);
        final DepositCommand command = new DepositCommand(
                accountId,
                new BigDecimal(request.amount)
        );
        commandHandler.handle(command)
                .consume(
                        balance -> respondJson(ctx, 200, balance),
                        error -> respondError(ctx, error.errorCode, error.name(), error.errorMessage));
    }
}
