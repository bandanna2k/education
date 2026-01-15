package education.maths;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

public class AbsoluteLongTest
{
    private final Random random = new SecureRandom("Random".getBytes(StandardCharsets.UTF_8));
    @Test
    void numberShouldBePositive()
    {
        for (int i = 0; i < 100; i++)
        {
            long value = random.nextLong();
            long abs1 = Math.abs(value);
            long abs2 = Math.abs(value);
            assertAbsoluteValues(abs1, abs2);
        }

        assertAbsoluteValues(0, Math.abs(0));
        assertAbsoluteValues(0, Absolute.abs(0));
    }
    private void assertAbsoluteValues(long abs1, long abs2)
    {
        Assertions.assertThat(abs1).isGreaterThanOrEqualTo(0);
        Assertions.assertThat(abs2).isGreaterThanOrEqualTo(0);
        Assertions.assertThat(abs1).isEqualTo(abs2);
    }
}
