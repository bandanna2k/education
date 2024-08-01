package education.syntax.doubleColon;

import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DoubleColonTest
{
    static class Maths
    {
        public static double square(double value)
        {
            return Math.pow(value, 2.0d);
        }
    }

    @Test
    public void test()
    {
        {
            assertThat(Maths.square(2.0d),is(equalTo(4.0d)));
        }
        {
            Function<Double, Double> square = Maths::square;
            assertThat(square.apply(2.0d),is(equalTo(4.0d)));
        }
        {
            Function<Double, Double> square = (Double x) -> Maths.square(x);
            assertThat(square.apply(2.0d),is(equalTo(4.0d)));
        }
    }
}
