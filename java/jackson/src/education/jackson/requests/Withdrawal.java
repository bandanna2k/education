package education.jackson.requests;

import java.math.BigDecimal;
import java.util.UUID;

import static education.jackson.NoAccountId.NO_ACCOUNT_ID;

public class Withdrawal extends Request
{
    public BigDecimal amount;
    public long accountId;

    public Withdrawal()
    {
        this(null, NO_ACCOUNT_ID, BigDecimal.ZERO);
    }
    public Withdrawal(UUID uuid, long accountId, String value)
    {
        this(uuid, accountId, new BigDecimal(value));
    }
    public Withdrawal(UUID uuid, long accountId, BigDecimal value)
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
        return "Withdrawal{" +
                "amount=" + amount +
                ", accountId=" + accountId +
                "} " + super.toString();
    }
}
