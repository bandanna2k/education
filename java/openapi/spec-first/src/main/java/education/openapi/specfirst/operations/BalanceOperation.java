package education.openapi.specfirst.operations;

import education.openapi.ApiOperation;
import education.openapi.specfirst.generated.api.BalanceApi;
import education.openapi.specfirst.generated.model.AccountRequest;
import education.openapi.specfirst.generated.model.Balance;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.Map;

import static education.openapi.specfirst.operations.ToBeRenamed.*;

public class BalanceOperation implements BalanceApi
{
    private final Map<Long, BigDecimal> balances;

    public BalanceOperation(Map<Long, BigDecimal> balances)
    {
        this.balances = balances;
    }

    private Balance getBalance(AccountRequest accountRequest)
    {
        BigDecimal amount = balances.getOrDefault(accountRequest.getAccountId(), BigDecimal.ZERO);
        return new Balance().balance(amount.toPlainString());
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
}
