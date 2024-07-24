package casestudy.bank;

import casestudy.bank.projections.Account;
import casestudy.bank.projections.AccountRepository;
import education.common.result.Result;
import education.jackson.requests.Deposit;
import education.jackson.requests.RequestVisitor;
import education.jackson.requests.Withdrawal;

import java.math.BigDecimal;

public class BankVisitor implements RequestVisitor
{
    private final AccountRepository accountRepository;

    public BankVisitor(final AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    @Override
    public void visit(Deposit deposit)
    {
        System.out.println("Deposit " + deposit);
        final Account account = accountRepository.getAccount(deposit.accountId);
        Result<BigDecimal, String> result = account.deposit(deposit.amount);
        System.out.println("Deposit result:" + result);
    }

    @Override
    public void visit(Withdrawal withdrawal)
    {
        System.out.println("Withdrawal " + withdrawal);
        final Account account = accountRepository.getAccount(withdrawal.accountId);
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
