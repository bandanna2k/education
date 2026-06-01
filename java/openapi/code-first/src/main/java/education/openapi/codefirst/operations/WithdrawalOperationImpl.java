package education.openapi.codefirst.operations;

import education.openapi.codefirst.operations.components.Balance;
import education.openapi.codefirst.operations.components.TransactionRequest;

import java.math.BigDecimal;
import java.util.Map;

public class WithdrawalOperationImpl implements WithdrawalOperation
{
    private final Map<String, BigDecimal> balances;

    public WithdrawalOperationImpl(Map<String, BigDecimal> balances)
    {
        this.balances = balances;
    }

    @Override
    public Balance execute(TransactionRequest request)
    {
        BigDecimal amount = new BigDecimal(request.amount);
        String accountId = request.accountId;
        BigDecimal current = balances.getOrDefault(accountId, BigDecimal.ZERO);
        if (current.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds: balance is " + current.toPlainString());
        }
        BigDecimal newBalance = balances.merge(accountId, amount.negate(), BigDecimal::add);
        return new Balance(newBalance.toPlainString());
    }
}
