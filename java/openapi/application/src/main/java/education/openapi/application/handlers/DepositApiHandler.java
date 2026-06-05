package education.openapi.application.handlers;

import education.openapi.api.DepositApi;
import education.openapi.model.TransactionRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;

import static education.openapi.application.ToBeRenamed.*;

public class DepositApiHandler implements DepositApi
{
    private final DepositCommandHandler commandHandler;

    public DepositApiHandler(DepositCommandHandler commandHandler)
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
                new BigDecimal(request.getAmount())
        );
        commandHandler.handle(command)
                .consume(
                        balance -> respondJson(ctx, 200, balance),
                        error -> respondError(ctx, error.errorCode, error.name(), error.errorMessage));
    }
}
