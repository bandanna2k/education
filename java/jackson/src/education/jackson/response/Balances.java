package education.jackson.response;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Balances extends Response
{
    public List<Balance> balances;

    public Balances()
    {
        this(null, Collections.emptyList());
    }
    public Balances(UUID uuid, List<Balance> balances)
    {
        super(uuid);
        balances.forEach(balance -> { assert balance.uuid == null; });
        this.balances = balances;
    }

    @Override
    public void visit(final ResponseVisitor visitor)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString()
    {
        return "Balances{" +
                "balances=" + balances +
                "} " + super.toString();
    }
}
