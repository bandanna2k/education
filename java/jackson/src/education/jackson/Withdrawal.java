package education.jackson;

import java.math.BigDecimal;

public class Withdrawal extends Message
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
        return "Withdrawal{" +
                "amount=" + amount +
                "} " + super.toString();
    }
}
