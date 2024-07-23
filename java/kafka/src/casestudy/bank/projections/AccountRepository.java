package casestudy.bank.projections;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountRepository
{
    final Map<Long, Account> accounts = new HashMap<>();

    public Account getAccount(final long accountId)
    {
        return accounts.get(accountId);
    }
}
