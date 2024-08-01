package education.ai.perceptron;


public abstract class Sigmoid
{

    // '' <summary>
    // '' Returns a value between 0 and 1 depending on any double value
    // '' </summary>
    public static double Sigmoid(double value)
    {
        return Sigmoid.sigmoid1(value);
    }

    private static double sigmoid1(double value)
    {
        return 1.0 / (1.0 + Math.exp(-value));
    }
}