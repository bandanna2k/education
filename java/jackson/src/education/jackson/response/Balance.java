package education.jackson.response;

import java.math.BigDecimal;
import java.util.UUID;

import static education.jackson.NoAccountId.NO_ACCOUNT_ID;

public class Balance extends Response
{
    public BigDecimal balance;
    public long accountId;

    public Balance()
    {
        this(null, NO_ACCOUNT_ID, BigDecimal.ZERO);
    }
    public Balance(UUID uuid, long accountId, String value)
    {
        this(uuid, accountId, new BigDecimal(value));
    }
    public Balance(UUID uuid, long accountId, BigDecimal value)
    {
        super(uuid);
        this.balance = value;
        this.accountId = accountId;
    }

    @Override
    public void visit(final ResponseVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return "Balance{" +
                "balance=" + balance +
                ", accountId=" + accountId +
                "} " + super.toString();
    }
}
