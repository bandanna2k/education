package education.openapi.specfirst.operations;

import education.openapi.specfirst.generated.api.WithdrawalApi;
import education.openapi.specfirst.generated.model.Balance;
import education.openapi.specfirst.generated.model.TransactionRequest;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.Map;

import static education.openapi.specfirst.operations.ToBeRenamed.*;

public class WithdrawalOperation implements WithdrawalApi
{
    private final Map<Long, BigDecimal> balances;

    public WithdrawalOperation(Map<Long, BigDecimal> balances)
    {
        this.balances = balances;
    }

    private Balance postWithdrawal(TransactionRequest transactionRequest)
    {
        BigDecimal amount = BigDecimal.valueOf(transactionRequest.getAmount());
        long accountId = transactionRequest.getAccountId();
        BigDecimal current = balances.getOrDefault(accountId, BigDecimal.ZERO);
        if (current.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds: balance is " + current.toPlainString());
        }
        BigDecimal newBalance = balances.merge(accountId, amount.negate(), BigDecimal::add);
        return new Balance().balance(newBalance.toPlainString());
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
