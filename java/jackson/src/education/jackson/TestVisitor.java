package education.jackson;

public class TestVisitor implements Visitor
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
