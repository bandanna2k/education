package education.openapi.codefirst.handlers;

import education.openapi.codefirst.operations.DepositOperation;
import education.openapi.codefirst.operations.components.TransactionRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;

import static education.openapi.codefirst.operations.ApiOperation.respondError;
import static education.openapi.codefirst.operations.ApiOperation.respondJson;

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
        final String accountId = ctx.pathParam("accountId");
//
//        final ValidatedRequest validatedRequest = ctx.get("KEY_META_DATA_VALIDATED_REQUEST");
        final JsonObject jsonBody = ctx.body().asJsonObject();

        final TransactionRequest request = jsonBody.mapTo(TransactionRequest.class);
        final DepositCommand command = new DepositCommand(
                Integer.parseInt(accountId),
                new BigDecimal(request.amount)
        );
        commandHandler.handle(command)
                .consume(
                        balance -> respondJson(ctx, 200, balance),
                        error -> respondError(ctx, error.errorCode, error.name(), error.errorMessage));
    }
}
