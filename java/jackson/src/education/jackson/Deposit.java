package education.jackson;

import java.math.BigDecimal;

import static education.jackson.NoAccountId.NO_ACCOUNT_ID;

public class Deposit extends Message
{
    public BigDecimal amount;
    public long accountId;

    public Deposit()
    {
        this(NO_ACCOUNT_ID, BigDecimal.ZERO);
    }
    public Deposit(long accountId, String value)
    {
        this(accountId, new BigDecimal(value));
    }
    public Deposit(long accountId, BigDecimal value)
    {
        super();
        this.amount = value;
        this.accountId = accountId;
    }

    @Override
    public void visit(MessageVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "amount=" + amount +
                ", accountId=" + accountId +
                "} " + super.toString();
    }
}
