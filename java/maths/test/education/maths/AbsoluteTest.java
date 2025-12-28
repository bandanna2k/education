package education.maths;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

public class AbsoluteTest
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
        assertAbsoluteValues(0, AbsoluteTest.abs(0));
    }
    private void assertAbsoluteValues(long abs1, long abs2)
    {
        assertThat(abs1).isGreaterThanOrEqualTo(0);
        assertThat(abs2).isGreaterThanOrEqualTo(0);
        assertThat(abs1).isEqualTo(abs2);
    }

    public static long abs(long value) {
        // Create a mask: -1 (all 1's in binary) for negative, 0 for positive
        // >> 63 for long (64-bit), shifts sign bit to all positions
        long mask = value >> 63;

        // XOR with mask: flips bits if negative, no change if positive
        // Then subtract mask: -(-1) = +1 for negatives, -(0) = 0 for positives
        return (value ^ mask) - mask;
    }
}
