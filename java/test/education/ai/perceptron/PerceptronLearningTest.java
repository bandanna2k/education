package education.ai.perceptron;

import maths.LinearEquations;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class PerceptronLearningTest
{
    private Random random;

    @Before
    public void setUp() throws Exception
    {
        random = new Random(1);
    }

    @Test
    public void shouldLearn()
    {
        shouldLearn(1, 10000);
        shouldLearn(2, 10000);
        shouldLearn(2, 20000);
    }

    private void shouldLearn(final int seed, final int learningCount)
    {
        Output output = new Output();
        Perceptron p1 = new PerceptronInputBuilder(random).build();

        Input inputX = new Input("X");
        Input inputY = new Input("Y");

        p1.setOutput(output);

        inputX.setOutput(p1);
        inputY.setOutput(p1);

        System.out.println(p1);

        // Learn
        Random random = new Random(seed);
        for (int i = 0; i < learningCount; i++)
        {
            int x = (int) ((random.nextDouble() - 0.5) * 20); // Gives a number between -10 and 10
            int y = (int) ((random.nextDouble() - 0.5) * 20); // Gives a number between -10 and 10

            boolean result = LinearEquations.XgreaterThanY(x, y);
            int nResult = result ? 0 : 1;

            inputX.fireLearningEvent(x, nResult);
            inputY.fireLearningEvent(y, nResult);
        }

//        p1.Logger = Nothing
        ask(inputX, -1, inputY, 1, output, "North West");
        ask(inputX, -0.5, inputY, 1, output, "North North West");
        ask(inputX, 0, inputY, 1, output, "North");
        ask(inputX, 0.5, inputY, 1, output, "North North East");
        ask(inputX, 1, inputY, 1, output, "North East");
        ask(inputX, 1, inputY, 0.5, output, "North East East");
        ask(inputX, 1, inputY, 0, output, "East");
        ask(inputX, 1, inputY, -0.5, output, "South East East");
        ask(inputX, 1, inputY, -1, output, "South East");
        ask(inputX, 0.5, inputY, -1, output, "South South West");
        ask(inputX, 0, inputY, -1, output, "South");
        ask(inputX, -0.5, inputY, -1, output, "South South West");
        ask(inputX, -1, inputY, -1, output, "South West");
        ask(inputX, -1, inputY, -0.5, output, "South West West");
        ask(inputX, -1, inputY, 0, output, "West");
        ask(inputX, -1, inputY, 0.5, output, "North West West");
    }

    private final void ask(Input inputX, double x, Input inputY, double y, Output output, String description)
    {
        inputX.fireValueEvent(x);
        inputY.fireValueEvent(y);
        System.out.println(String.format("Ask X:%f, Y:%f, Result:%f, Description:%s", x, y, output.value.getAsDouble(), description));
    }
}