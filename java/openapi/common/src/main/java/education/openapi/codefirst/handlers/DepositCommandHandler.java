package education.openapi.codefirst.handlers;

import education.common.result.Result;
import education.openapi.codefirst.operations.ApiError;
import education.openapi.codefirst.operations.components.Balance;

import java.math.BigDecimal;
import java.util.Map;

public class DepositCommandHandler
{
    private final Map<Integer, BigDecimal> balances;

    public DepositCommandHandler(Map<Integer, BigDecimal> balances)
    {
        this.balances = balances;
    }

    public Result<Balance, ApiError> handle(DepositCommand command)
    {
        BigDecimal amount = command.amount();
        BigDecimal newBalance = balances.merge(
                command.accountId(), amount, BigDecimal::add);
        return Result.success(new Balance(newBalance.toPlainString()));
    }
}
