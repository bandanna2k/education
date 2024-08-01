package education.ai.perceptron;

import java.util.Random;

public abstract class Weight
{
    public static double randomWeight(final Random random)
    {
        return ((random.nextDouble() * 2) - 1);
    }
}
