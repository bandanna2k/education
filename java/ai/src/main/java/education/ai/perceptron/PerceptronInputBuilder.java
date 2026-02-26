package education.ai.perceptron;

import java.util.Random;

import static education.ai.perceptron.ActivationFunction.SigmoidFunction;

public class PerceptronInputBuilder
{
    private final Random random;
    private final double learningRate = 0.01;
    private final ActivationFunction activationFunction = SigmoidFunction;
    private double bias;

    public PerceptronInputBuilder(Random random)
    {
        this.random = random;
    }

    public Perceptron build()
    {
        final Perceptron perceptron = new Perceptron(random, learningRate, activationFunction);
        perceptron.setBias(bias);
        return perceptron;
    }

    public PerceptronInputBuilder bias(final double bias)
    {
        this.bias = bias;
        return this;
    }
}
