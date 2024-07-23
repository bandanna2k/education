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

    public void deposit(final BigDecimal amount)
    {
        balance = balance.add(amount);
    }

    public void withdraw(final BigDecimal amount)
    {
        balance = balance.subtract(amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                '}';
    }
}
