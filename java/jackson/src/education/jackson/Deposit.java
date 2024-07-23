package education.jackson;

import java.math.BigDecimal;

public class Deposit extends Message
{
    public BigDecimal amount;

    public Deposit()
    {
        this(BigDecimal.ZERO);
    }
    public Deposit(String value)
    {
        this(new BigDecimal(value));
    }
    public Deposit(BigDecimal value)
    {
        super();
        amount = value;
    }

    @Override
    public void visit(MessageVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return "Deposit{" +
                "amount=" + amount +
                "} " + super.toString();
    }
}
