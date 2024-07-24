package casestudy.bank.projections;

import education.common.result.Result;

import java.math.BigDecimal;

public class Account
{
    public final long accountId;
    public BigDecimal balance = BigDecimal.ZERO;

    public Account(final long accountId)
    {
        this.accountId = accountId;
    }

    public Result<BigDecimal, String> deposit(final BigDecimal amount)
    {
        balance = balance.add(amount);
        return Result.success(balance);
    }

    public Result<BigDecimal, String> withdraw(final BigDecimal amount)
    {
        BigDecimal newBalance = balance.subtract(amount);
        if(newBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            return Result.failure("Not enough equity to withdraw.");
        }
        balance = newBalance;
        return Result.success(balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                '}';
    }
}
