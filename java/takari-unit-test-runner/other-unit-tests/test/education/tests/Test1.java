package education.tests;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class Test1
{
    @Test
    public void shouldTestAdditionAxiom()
    {
        System.out.println("shouldTestAdditionAxiom");
        assertThat(1 + 1).isEqualTo(2);
    }
}
