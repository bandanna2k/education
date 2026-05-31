package education.openapi.codefirst.operations;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.Map;

import static education.openapi.codefirst.operations.ApiOperation.*;

public class WithdrawalOperationImpl implements WithdrawalOperation
{
    private final Map<String, BigDecimal> balances;

    public WithdrawalOperationImpl(Map<String, BigDecimal> balances)
    {
        this.balances = balances;
    }

    private Balance postWithdrawal(TransactionRequest transactionRequest)
    {
        BigDecimal amount = new BigDecimal(transactionRequest.amount);
        String accountId = transactionRequest.accountId;
        BigDecimal current = balances.getOrDefault(accountId, BigDecimal.ZERO);
        if (current.compareTo(amount) < 0) {
            throw new education.openapi.codefirst.operations.InsufficientFundsException("Insufficient funds: balance is " + current.toPlainString());
        }
        BigDecimal newBalance = balances.merge(accountId, amount.negate(), BigDecimal::add);
        return new Balance(newBalance.toPlainString());
    }

    @Override
    public void handle(RoutingContext ctx)
    {
        try {
            TransactionRequest req = MAPPER.readValue(ctx.body().asString(), TransactionRequest.class);
            Balance result = postWithdrawal(req);
            respondJson(ctx, 200, result);
        } catch (InsufficientFundsException e) {
            respondError(ctx, 400, "INSUFFICIENT_FUNDS", e.getMessage());
        } catch (Exception e) {
            respondError(ctx, 400, "BAD_REQUEST", e.getMessage());
        }
    }
}
