package casestudy.bank.projections;

import casestudy.bank.RequestRegistry;
import education.common.result.Result;
import education.jackson.requests.Deposit;
import education.jackson.requests.Withdrawal;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class AccountRepository implements RequestRegistry.DepositListener, RequestRegistry.WithdrawalListener
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

    @Override
    public void onDeposit(Deposit deposit)
    {
        System.out.println("Deposit " + deposit);

        Account account = accounts.get(deposit.accountId);
        Result<BigDecimal, String> result = account.deposit(deposit.amount);
        System.out.println("Deposit result:" + result);
    }

    @Override
    public void onWithdrawal(Withdrawal withdrawal)
    {
        System.out.println("Withdrawal " + withdrawal);

        Account account = accounts.get(withdrawal.accountId);
        Result<BigDecimal, String> result = account.withdraw(withdrawal.amount);
        result.fold(success ->
        {
            System.out.println("After withdrawal balance:" + success);
        }, error ->
        {
            System.out.println("Failed to withdraw:" + error);
        });
    }
}
