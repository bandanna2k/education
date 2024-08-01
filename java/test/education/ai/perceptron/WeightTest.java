package education.ai.perceptron;

import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class WeightTest
{
    @Test
    public void shouldAlwaysBeBetweenOneAndMinusOne()
    {
        final Random random = new Random();
        for (int i = 0; i < 1000; i++)
        {
            final double weight = Weight.randomWeight(random);
            assertThat(weight).isBetween(Double.valueOf(-1), Double.valueOf(1));
        }
    }
}