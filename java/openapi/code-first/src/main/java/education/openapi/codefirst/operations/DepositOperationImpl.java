package education.openapi.codefirst.operations;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.Map;

import static education.openapi.codefirst.operations.ApiOperation.respondError;
import static education.openapi.codefirst.operations.ApiOperation.respondJson;

public class DepositOperationImpl implements DepositOperation
{
    private final Map<String, BigDecimal> balances;

    public DepositOperationImpl(Map<String, BigDecimal> balances)
    {
        this.balances = balances;
    }

    private Balance postDeposit(TransactionRequest transactionRequest)
    {
        BigDecimal amount = new BigDecimal(transactionRequest.amount);
        BigDecimal newBalance = balances.merge(
                transactionRequest.accountId, amount, BigDecimal::add);
        return new Balance(newBalance.toPlainString());
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
