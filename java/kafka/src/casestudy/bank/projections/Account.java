package casestudy.bank.projections;

import java.math.BigDecimal;

public class Account
{
    public BigDecimal balance;

    public void deposit(final BigDecimal amount)
    {
        balance.add(amount);
    }

    public void withdraw(final BigDecimal amount)
    {
        balance.subtract(amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                '}';
    }
}
