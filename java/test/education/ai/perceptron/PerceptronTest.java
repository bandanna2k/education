package education.ai.perceptron;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static education.ai.perceptron.ActivationFunction.SigmoidFunction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class PerceptronTest
{
    private Random random;

    @Before
    public void setUp() throws Exception
    {
        random = new Random(1);
    }

    @Test
    public void shouldOutputFromInput()
    {
        Output output = new Output();
        Perceptron p1 = new PerceptronInputBuilder(random).build();

        Input input1 = new Input("X");
        Input input2 = new Input("Y");

        p1.setOutput(output);

        input1.setOutput(p1);
        input2.setOutput(p1);

        // Fire all inputs
        input1.fireValueEvent(1);
        input2.fireValueEvent(2);

        assertTrue(output.value.isPresent());
    }

    @Test
    public void shouldOutputFromChain()
    {
        Output output = new Output();

        Perceptron p1 = new PerceptronInputBuilder(random).build();
        Perceptron p2 = new PerceptronInputBuilder(random).build();

//        p1.Logger =Console.Out
//        p2.Logger =Console.Out

        Input input1 = new Input("X");
        Input input2 = new Input("Y");

        p1.setOutput(output);
        p2.setOutput(output);

        p1.setOutput(p2);

        input1.setOutput(p1);
        input2.setOutput(p1);

        // Fire all inputs
        input1.fireValueEvent(1);
        input2.fireValueEvent(2);

        assertTrue(output.value.isPresent());
    }

    @Test
    public void shouldFireLearningEvents()
    {
        Output output = new Output();

        MockPerceptron p1 = new MockPerceptron(random, 0.01, SigmoidFunction);
        MockPerceptron p2 = new MockPerceptron(random, 0.01, SigmoidFunction);

//        p1.Logger =Console.Out
//        p2.Logger =Console.Out

        Input input1 = new Input("X");
        Input input2 = new Input("Y");

        p1.setOutput(output);
        p2.setOutput(output);

        p1.setOutput(p2);

        input1.setOutput(p1);
        input2.setOutput(p1);

        // Fire all inputs
        input1.fireLearningEvent(1, 1);
        input2.fireLearningEvent(2, 1);

        assertThat(p1.countOfLearningEventsFired).isEqualTo(1);
        assertThat(p2.countOfLearningEventsFired).isEqualTo(1);
    }

    @Test
    public void shouldFireLearningEventsInChain()
    {
        Output output = new Output();

        MockPerceptron p1 = new MockPerceptron(random, 0.01, SigmoidFunction);
        MockPerceptron p2 = new MockPerceptron(random, 0.01, SigmoidFunction);

//        p1.Logger =Console.Out
//        p2.Logger =Console.Out

        Input input1 = new Input("X");
        Input input2 = new Input("Y");

        input1.setOutput(p1);
        input2.setOutput(p1);

        p1.setOutput(p2);

        p2.setOutput(output);

        // Fire all inputs
        input1.fireLearningEvent(1, 1);
        input2.fireLearningEvent(2, 1);

        assertThat(p1.countOfLearningEventsFired).isEqualTo(1);
        assertThat(p2.countOfLearningEventsFired).isEqualTo(1);
    }

    @Test
    public void shouldLearnTwiceWhenLearningTwice()
    {
        Output output = new Output();

        MockPerceptron p1 = new MockPerceptron(random, 0.01, SigmoidFunction);

//        p1.Logger =Console.Out
//        p2.Logger =Console.Out

        Input input1 = new Input("X");
        Input input2 = new Input("Y");

        input1.setOutput(p1);
        input2.setOutput(p1);

        p1.setOutput(output);

        // Fire all inputs
        input1.fireLearningEvent(1, 1);
        input2.fireLearningEvent(2, 1);

        // Fire all inputs
        input1.fireLearningEvent(-1, 0);
        input2.fireLearningEvent(-2, 0);

        assertThat(p1.countOfLearningEventsFired).isEqualTo(2);
    }

    @Test
    public void shouldBuildWithBias()
    {
        Output output = new Output();
        Perceptron p1 = new PerceptronInputBuilder(random).bias(1.0).build();

        Input input1 = new Input("X");
        Input input2 = new Input("Y");

        p1.setOutput(output);

        input1.setOutput(p1);
        input2.setOutput(p1);

        // Fire all inputs
        input1.fireValueEvent(1);
        input2.fireValueEvent(2);

        System.out.println(p1);

        assertTrue(output.value.isPresent());
    }

}