package education.openapi.codefirst.operations;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;

import java.math.BigDecimal;
import java.util.Map;

public class DepositOperationImpl implements DepositOperation
{
    private final Map<String, BigDecimal> balances;

    public DepositOperationImpl(Map<String, BigDecimal> balances)
    {
        this.balances = balances;
    }

    @Override
    public Balance execute(TransactionRequest request)
    {
        BigDecimal amount = new BigDecimal(request.amount);
        BigDecimal newBalance = balances.merge(
                request.accountId, amount, BigDecimal::add);
        return new Balance(newBalance.toPlainString());
    }
}
