package education.designpatterns.visitorPattern.differentPackage;

public class Account
{
    private int profitLoss = 10;

    int getProfitLoss()
    {
        return profitLoss;
    }

    public void accept(AccountVisitor cpv)
    {
        cpv.visit(this);
    }
}
