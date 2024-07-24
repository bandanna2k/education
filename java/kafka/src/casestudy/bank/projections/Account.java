package casestudy.bank.projections;

import java.math.BigDecimal;

public class Account
{
    public final long accountId;
    public BigDecimal balance = BigDecimal.ZERO;

    public Account(final long accountId)
    {
        this.accountId = accountId;
    }

    public boolean deposit(final BigDecimal amount)
    {
        BigDecimal newBalance = balance.add(amount);
        balance = newBalance;
        return true;
    }

    public boolean withdraw(final BigDecimal amount)
    {
        BigDecimal newBalance = balance.subtract(amount);
        if(newBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            return false;
        }
        balance = newBalance;
        return true;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                '}';
    }
}
