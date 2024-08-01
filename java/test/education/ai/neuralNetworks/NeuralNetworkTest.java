package education.ai.neuralNetworks;

import org.junit.Test;

public class NeuralNetworkTest extends NeuralNetworkBase
{
    @Test
    public void shouldBuildNeuralNetwork()
    {
        buildNeuralNetwork("name: net", "layout: 2, 3, 3");
        input("name: net", "input: 1, 1");
        verifyOutput("name: net", "output: 0", "tolerance: 1");
    }

    @Test
    public void shouldExampleNeuralNetwork()
    {
        buildNeuralNetwork("name: net",
                "layout: 2, 2",
                "weights: -1.5, 2.1, 1.4, 0.6,  1.1, 0.9");
        input("name: net", "input: 0.8, 0.7");
        verifyOutput("name: net", "output: 0", "tolerance: 1");
    }
}
