package education.vertx.json.subtypes.unannotated.requests;

import java.math.BigDecimal;
import java.util.UUID;

public class Withdrawal extends Request
{
    public BigDecimal amount;
    public long accountId;

    public Withdrawal()
    {
        this(null, -1, BigDecimal.ZERO);
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
    public String toString() {
        return "Withdrawal{" +
                "amount=" + amount +
                ", accountId=" + accountId +
                "} " + super.toString();
    }
}
