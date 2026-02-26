package education.ai.neuralNetworks;

import education.ai.perceptron.ActivationFunction;
import education.ai.perceptron.Input;
import education.ai.perceptron.Output;
import education.ai.perceptron.Perceptron;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

public class NeuralNetwork
{
    final List<Input> inputs = new ArrayList<>();
    final List<List<Perceptron>> layers = new ArrayList<>();
    final Output output = new Output();

    public NeuralNetwork(final Random random,
                         final int[] layout,
                         final double learningRate,
                         final ActivationFunction activationFunction)
    {
        // Build first layer
        for (int i = 0; i < layout[0]; i++)
        {
            inputs.add(new Input("Input" + (i + 1)));
        }

        // Build all other layers
        for (int i = 1; i < layout.length ; i++)
        {
            final int layerSize = layout[i];

            List<Perceptron> currentLayer = new ArrayList<>();
            for (int j = 0; j < layerSize; j++)
            {
                final Perceptron perceptron = new Perceptron(random, learningRate, activationFunction);
                currentLayer.add(perceptron);
            }
            layers.add(currentLayer);
        }

        setOutputs();
    }

    private void setOutputs()
    {
        // Set first layer
        inputs.forEach(input ->
        {
            layers.get(0).stream().forEach(l1 -> input.setOutput(l1));
        });

        // Set other layers
        final int layerCount = layers.size();
        for (int i = 1; i < layerCount; i++)
        {
            for (int previousPerceptronIndex = 0; previousPerceptronIndex < layers.get(i-1).size(); previousPerceptronIndex++)
            {
                for (int currentPerceptronIndex = 0; currentPerceptronIndex < layers.get(i).size(); currentPerceptronIndex++)
                {
                    Perceptron p0 = layers.get(i -1).get(previousPerceptronIndex);
                    Perceptron p1 = layers.get(1).get(currentPerceptronIndex);

                    p0.setOutput(p1);
                }
            }
        }

        // Set output
        List<Perceptron> lastLayer = layers.get(layers.size() - 1);
        for (int i = 0; i < lastLayer.size(); i++)
        {
            Perceptron p = lastLayer.get(i);
            p.setOutput(output);
        }
    }


    public OptionalDouble getOutput()
    {
        return output.value;
    }

    public void input(final double[] input)
    {
        if(input.length != this.inputs.size())
        {
            throw new RuntimeException(String.format("Input count (%d) does not match first layer count (%d).", input.length, layers.size()));
        }

        for (int i = 0; i < input.length; i++)
        {
            inputs.get(i).fireValueEvent(input[i]);
        }
    }

    public void setWeights(final double[] weights)
    {
        int pointer = 0;

        // Set hidden weights
        layers.forEach(layer -> {
            for (int i = 0; i < layer.size(); i++)
            {
                layer.get(i).setWeights();
            }
        });

        // Set output weights
    }
}
