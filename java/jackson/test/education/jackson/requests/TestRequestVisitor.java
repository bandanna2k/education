package education.jackson.requests;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestRequestVisitor implements RequestVisitor
{
    private final UUID expectedUuid;

    public TestRequestVisitor(UUID expectedUuid)
    {
        this.expectedUuid = expectedUuid;
    }

    @Override
    public void visit(Deposit deposit)
    {
        System.out.println("Deposit " + deposit);
        assertThat(deposit).isInstanceOf(Deposit.class);
        assertThat(deposit.type).isEqualTo("deposit");
        assertThat(deposit.uuid).isEqualTo(expectedUuid);
    }

    @Override
    public void visit(Withdrawal withdrawal)
    {
        System.out.println("Deposit " + withdrawal);
        assertThat(withdrawal).isInstanceOf(Withdrawal.class);
        assertThat(withdrawal.type).isEqualTo("withdrawal");
        assertThat(withdrawal.uuid).isEqualTo(expectedUuid);
    }
}
