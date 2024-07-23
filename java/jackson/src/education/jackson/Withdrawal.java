package education.jackson;

import java.math.BigDecimal;

public class Withdrawal extends Message
{
    private static final String TYPE = "withdrawal";

    public BigDecimal amount;

    public Withdrawal()
    {
        this(BigDecimal.ZERO);
    }
    public Withdrawal(String value)
    {
        this(new BigDecimal(value));
    }
    public Withdrawal(BigDecimal value)
    {
        this.type = TYPE;
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
        return "Withdrawal{" +
                "amount=" + amount +
                "} " + super.toString();
    }
}
