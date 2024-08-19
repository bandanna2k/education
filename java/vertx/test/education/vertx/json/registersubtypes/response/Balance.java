package education.vertx.json.registersubtypes.response;

import java.math.BigDecimal;
import java.util.UUID;

public class Balance extends Response
{
    public BigDecimal balance;
    public long accountId;

    public Balance()
    {
        this(null, -1, BigDecimal.ZERO);
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
    public String toString()
    {
        return "Balance{" +
                "balance=" + balance +
                ", accountId=" + accountId +
                "} " + super.toString();
    }
}
