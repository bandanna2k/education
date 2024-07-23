package casestudy.bank;

import education.jackson.Deposit;
import education.jackson.MessageVisitor;
import education.jackson.Withdrawal;

public class BankVisitor implements MessageVisitor
{
    @Override
    public void visit(Deposit deposit)
    {
        System.out.println("Deposit " + deposit);
    }

    @Override
    public void visit(Withdrawal withdrawal)
    {
        System.out.println("Withdrawal " + withdrawal);
    }
}
