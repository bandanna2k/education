package education.jackson.response;

import java.math.BigDecimal;
import java.util.UUID;

import static education.jackson.NoAccountId.NO_ACCOUNT_ID;

public class Error extends Response
{
    public String error;

    public Error()
    {
        this(null, null);
    }
    public Error(UUID uuid, String error)
    {
        super(uuid);
        this.error = error;
    }

    @Override
    public void visit(final ResponseVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return "Error{" +
                "error='" + error + '\'' +
                "} " + super.toString();
    }
}
