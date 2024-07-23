package casestudy.bank.projections;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class AccountRepository
{
    final Map<Long, Account> accounts = new TreeMap<>();

    public Account getAccount(final long accountId)
    {
        return accounts.get(accountId);
    }

    @Override
    public String toString() {
        return "AccountRepository{" +
                "accounts=" + accounts +
                '}';
    }

    public void foreach(final Consumer<Account> action)
    {
        accounts.forEach((accountId, account) -> action.accept(account));
    }

    public void addAccount(final Account account)
    {
        accounts.put(account.accountId, account);
    }
}
