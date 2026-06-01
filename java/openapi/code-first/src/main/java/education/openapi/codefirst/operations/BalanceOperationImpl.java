package education.openapi.codefirst.operations;

import education.openapi.codefirst.operations.components.AccountRequest;
import education.openapi.codefirst.operations.components.Balance;

import java.math.BigDecimal;
import java.util.Map;

public class BalanceOperationImpl implements BalanceOperation
{
    private final Map<String, BigDecimal> balances;

    public BalanceOperationImpl(Map<String, BigDecimal> balances)
    {
        this.balances = balances;
    }

    @Override
    public Balance execute(AccountRequest req)
    {
        BigDecimal amount = balances.getOrDefault(req.accountId, BigDecimal.ZERO);
        return new Balance(amount.toPlainString());
    }
}
