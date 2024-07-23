package education.jackson;

import java.math.BigDecimal;

public class Deposit extends Message
{
    public BigDecimal amount;

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
