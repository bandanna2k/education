package education.jackson;

import static org.assertj.core.api.Assertions.assertThat;

public class TestVisitor implements MessageVisitor
{
    @Override
    public void visit(Deposit deposit)
    {
        System.out.println("Deposit " + deposit);
        assertThat(deposit).isInstanceOf(Deposit.class);
        assertThat(deposit.type).isEqualTo("deposit");
    }

    @Override
    public void visit(Withdrawal withdrawal)
    {
        System.out.println("Deposit " + withdrawal);
        assertThat(withdrawal).isInstanceOf(Withdrawal.class);
        assertThat(withdrawal.type).isEqualTo("withdrawal");
    }
}
