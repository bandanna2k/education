package education.ai.perceptron;

import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SigmoidTest
{
    @Test
    public void shouldOutput()
    {
        for (int i = -4; i < 4; i++)
        {
            System.out.println(i + " " + Sigmoid.Sigmoid(i));
        }

        shouldOutput(-3, 0.05);
        shouldOutput(-2, 0.11);
        shouldOutput(-1, 0.27);
        shouldOutput(0, 0.5);
        shouldOutput(1, 0.73);
        shouldOutput(2, 0.89);
        shouldOutput(3, 0.95);
    }

    private void shouldOutput(double input, double expectedOutput)
    {
        assertThat(Sigmoid.Sigmoid(input)).isCloseTo(expectedOutput, Offset.offset(0.01));
    }
}
