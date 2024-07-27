package education.maths;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalTest
{
    @Test
    public void testDoubleValueWithNegativeNumbers()
    {
        BigDecimal minus1 = new BigDecimal("-1");
        assertThat(minus1.doubleValue()).isEqualTo(-1.0);
    }
}
