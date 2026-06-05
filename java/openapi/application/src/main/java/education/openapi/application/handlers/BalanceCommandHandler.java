package education.openapi.application.handlers;

import education.common.result.Result;
import education.openapi.application.ApiError;
import education.openapi.model.Balance;

import java.math.BigDecimal;
import java.util.Map;

public class BalanceCommandHandler
{
    private final Map<Integer, BigDecimal> balances;

    public BalanceCommandHandler(Map<Integer, BigDecimal> balances)
    {
        this.balances = balances;
    }

    public Result<Balance, ApiError> handle(int accountId)
    {
        BigDecimal amount = balances.getOrDefault(accountId, BigDecimal.ZERO);
        return Result.success(new Balance().balance(amount.toPlainString()));
    }
}
