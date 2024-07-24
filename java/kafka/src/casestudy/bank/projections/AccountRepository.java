package casestudy.bank.projections;

import casestudy.bank.Publisher;
import casestudy.bank.RequestRegistry;
import education.common.result.Result;
import education.jackson.requests.Deposit;
import education.jackson.requests.Withdrawal;
import education.jackson.response.Balance;
import education.jackson.response.Error;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class AccountRepository implements RequestRegistry.DepositListener, RequestRegistry.WithdrawalListener
{
    private final Map<Long, Account> accounts = new TreeMap<>();
    private final Publisher publisher;

    public AccountRepository(Publisher publisher)
    {
        this.publisher = publisher;
    }

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

    @Override
    public void onDeposit(Deposit deposit)
    {
        System.out.println("Deposit " + deposit);

        Account account = accounts.get(deposit.accountId);
        Result<BigDecimal, String> result = account.deposit(deposit.amount);

        publisher.publishResponse(new Balance(deposit.uuid, account.accountId, account.balance));

        System.out.println("Deposit result:" + result);
    }

    @Override
    public void onWithdrawal(Withdrawal withdrawal)
    {
        System.out.println("Withdrawal " + withdrawal);

        Account account = accounts.get(withdrawal.accountId);
        Result<BigDecimal, String> result = account.withdraw(withdrawal.amount);
        result.fold(balance ->
        {
            publisher.publishResponse(new Balance(withdrawal.uuid, account.accountId, balance));
        }, error ->
        {
            publisher.publishResponse(new Error(withdrawal.uuid, error));
        });
    }
}
