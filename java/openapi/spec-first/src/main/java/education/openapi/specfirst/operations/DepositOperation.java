package education.openapi.specfirst.operations;

import education.openapi.specfirst.generated.api.DepositApi;
import education.openapi.specfirst.generated.model.Balance;
import education.openapi.specfirst.generated.model.TransactionRequest;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.Map;

import static education.openapi.specfirst.operations.ToBeRenamed.*;

public class DepositOperation implements DepositApi
{
    private final Map<Long, BigDecimal> balances;

    public DepositOperation(Map<Long, BigDecimal> balances)
    {
        this.balances = balances;
    }

    private Balance postDeposit(TransactionRequest transactionRequest)
    {
        BigDecimal amount = BigDecimal.valueOf(transactionRequest.getAmount());
        BigDecimal newBalance = balances.merge(
                transactionRequest.getAccountId(), amount, BigDecimal::add);
        return new Balance().balance(newBalance.toPlainString());
    }

    @Override
    public void handle(RoutingContext ctx)
    {
        try {
            TransactionRequest req = MAPPER.readValue(ctx.body().asString(), TransactionRequest.class);
            Balance result = postDeposit(req);
            respondJson(ctx, 200, result);
        } catch (Exception e) {
            respondError(ctx, 400, "BAD_REQUEST", e.getMessage());
        }
    }
}
