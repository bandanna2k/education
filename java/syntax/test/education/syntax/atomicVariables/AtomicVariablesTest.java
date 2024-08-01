package education.syntax.atomicVariables;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


public class AtomicVariablesTest
{
    @Test
    public void shouldQueryAtomicVariable()
    {
        AtomicInteger atomicInteger = new AtomicInteger(100);
        assertThat(100).isEqualTo(atomicInteger.getAndAdd(-10));
        assertThat(90).isEqualTo(atomicInteger.getAndAdd(-10));
    }
}
