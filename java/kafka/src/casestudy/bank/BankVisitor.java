package casestudy.bank;

import casestudy.bank.projections.Account;
import casestudy.bank.projections.AccountRepository;
import education.jackson.Deposit;
import education.jackson.MessageVisitor;
import education.jackson.Withdrawal;

public class BankVisitor implements MessageVisitor
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
        account.deposit(deposit.amount);
    }

    @Override
    public void visit(Withdrawal withdrawal)
    {
        System.out.println("Withdrawal " + withdrawal);
        final Account account = accountRepository.getAccount(withdrawal.accountId);
        account.withdraw(withdrawal.amount);
    }
}
