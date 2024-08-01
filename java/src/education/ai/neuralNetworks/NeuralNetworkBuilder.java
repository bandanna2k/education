package education.ai.neuralNetworks;

import education.ai.perceptron.ActivationFunction;

import java.util.Random;

import static education.ai.perceptron.ActivationFunction.SigmoidFunction;

public class NeuralNetworkBuilder
{
    private final Random random;
    private final int[] layout;
    private final double learningRate = 0.01;
    private final ActivationFunction activationFunction = SigmoidFunction;
    private double[] weights;

    public NeuralNetworkBuilder(final Random random, final int[] layout)
    {
        this.random = random;
        this.layout = layout;
    }

    public NeuralNetwork build()
    {
        final NeuralNetwork neuralNetwork = new NeuralNetwork(random, layout, learningRate, activationFunction);
        if(weights != null && weights.length != 0)
        {
            neuralNetwork.setWeights(weights);
        }
        return neuralNetwork;
    }

    public NeuralNetworkBuilder weights(final double[] weights)
    {
        this.weights = weights;
        return this;
    }
}
