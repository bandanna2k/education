package education.syntax;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CastingTest
{
    @Test
    public void shouldCastLongToInt()
    {
        final long l = 123123412412L;
        final int i = (int)l;
        assertThat(i).isEqualTo(-1430639172);
    }
}
