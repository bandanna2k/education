package education.ai.perceptron;

import java.util.OptionalDouble;

public class Output implements PerceptronEvents
{
    public OptionalDouble value = OptionalDouble.empty();

    public void valueEvent(Object sender, double value)
    {
        this.value = OptionalDouble.of(value);
    }

    public void newSubscriptionEvent(Object subscriber)
    {
        // Do nothing. Not interested in the new subscription event
    }

    public void learningEvent(Object sender, double output)
    {
        // Do nothing. Not interest in learning
    }
}
