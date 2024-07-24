package education.jackson.requests;

import education.jackson.Message;
import education.jackson.MessageVisitor;

import java.math.BigDecimal;
import java.util.UUID;

import static education.jackson.NoAccountId.NO_ACCOUNT_ID;

public class Deposit extends Request
{
    public BigDecimal amount;
    public long accountId;

    public Deposit()
    {
        this(null, NO_ACCOUNT_ID, BigDecimal.ZERO);
    }
    public Deposit(UUID uuid, long accountId, String value)
    {
        this(uuid, accountId, new BigDecimal(value));
    }
    public Deposit(UUID uuid, long accountId, BigDecimal value)
    {
        super(uuid);
        this.amount = value;
        this.accountId = accountId;
    }

    @Override
    public void visit(RequestVisitor visitor)
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
