package education.tests;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Test1
{
    @Test
    public void shouldTestAdditionAxiom()
    {
        System.out.println("shouldTestAdditionAxiom");
        assertThat(1 + 1).isEqualTo(2);
    }
}
