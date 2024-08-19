package education.vertx.json.subtypes.unannotated.requests;

import java.math.BigDecimal;
import java.util.UUID;

public class Deposit extends Request
{
    public BigDecimal amount;
    public long accountId;

    public Deposit()
    {
        this(null, -1, BigDecimal.ZERO);
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
    public String toString() {
        return "Deposit{" +
                "amount=" + amount +
                ", accountId=" + accountId +
                "} " + super.toString();
    }
}
