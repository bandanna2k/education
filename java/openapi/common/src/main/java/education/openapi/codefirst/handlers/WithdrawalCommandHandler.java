package education.openapi.codefirst.handlers;

import education.common.result.Result;
import education.openapi.codefirst.operations.ApiError;
import education.openapi.codefirst.operations.components.Balance;

import java.math.BigDecimal;
import java.util.Map;

import static education.common.result.Result.success;

public class WithdrawalCommandHandler
{
    private final Map<Integer, BigDecimal> balances;

    public WithdrawalCommandHandler(Map<Integer, BigDecimal> balances)
    {
        this.balances = balances;
    }

    public Result<Balance, ApiError> handle(WithdrawalCommand command)
    {
        BigDecimal amount = command.amount();
        int accountId = command.accountId();
        BigDecimal current = balances.getOrDefault(accountId, BigDecimal.ZERO);
        if (current.compareTo(amount) < 0) {
            return Result.failure(ApiError.INSUFFICIENT_FUNDS);
        }
        BigDecimal newBalance = balances.merge(accountId, amount.negate(), BigDecimal::add);
        return success(new Balance(newBalance.toPlainString()));
    }
}
