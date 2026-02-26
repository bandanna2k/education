package education.designpatterns.visitorPattern.differentPackage;

public class AccountVisitor
{
    private int balance;

    public int getBalance() { return balance; }

    public void visit(Account c)
    {
        this.balance = c.getProfitLoss();
    }
}
