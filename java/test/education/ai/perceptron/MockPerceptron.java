package education.ai.perceptron;

import java.util.Random;

public class MockPerceptron extends Perceptron
{
    public int countOfLearningEventsFired = 0;

    public MockPerceptron(final Random random, final double learningRate, final ActivationFunction activationFunction)
    {
        super(random, learningRate, activationFunction);
    }

    @Override
    protected void fireLearningEvent(final double output)
    {
        super.fireLearningEvent(output);
        countOfLearningEventsFired++;
    }
}
