package education.syntax.enums;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumsTest
{
    private enum MyNumber
    {
        ZERO,
        ONE(1);

        public final int value;

        MyNumber()
        {
            this(0);
        }
        MyNumber(final int i)
        {
            this.value = i;
        }
    }


    @Test
    public void testEnums()
    {
        assertThat(MyNumber.ZERO.value).isEqualTo(0);
        assertThat(MyNumber.ONE.value).isEqualTo(1);
    }


}
