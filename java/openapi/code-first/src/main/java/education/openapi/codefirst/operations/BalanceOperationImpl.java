package education.openapi.codefirst.operations;

import education.openapi.codefirst.components.AccountRequest;
import education.openapi.codefirst.components.Balance;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.Map;

import static education.openapi.codefirst.operations.ApiOperation.*;

public class BalanceOperationImpl implements BalanceOperation
{
    private final Map<String, BigDecimal> balances;

    public BalanceOperationImpl(Map<String, BigDecimal> balances)
    {
        this.balances = balances;
    }

    @Override
    public void handle(RoutingContext ctx) {
        try {
            AccountRequest req = MAPPER.readValue(ctx.body().asString(), AccountRequest.class);
            Balance result = getBalance(req);
            respondJson(ctx, 200, result);
        } catch (Exception e) {
            respondError(ctx, 400, "BAD_REQUEST", e.getMessage());
        }
    }

    private Balance getBalance(AccountRequest req)
    {
        BigDecimal amount = balances.getOrDefault(req.accountId, BigDecimal.ZERO);
        return new Balance(amount.toPlainString());
    }
}
