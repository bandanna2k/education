package education.jackson;

public class TestVisitor implements MessageVisitor
{
    @Override
    public void visit(Deposit deposit)
    {
        System.out.println("Deposit " + deposit);
    }

    @Override
    public void visit(Withdrawal withdrawal)
    {
        System.out.println("Deposit " + withdrawal);
    }
}
