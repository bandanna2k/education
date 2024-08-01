package education.ai.perceptron;

import java.io.OutputStream;

public class Input
{
    private final SubscriberManager<PerceptronEvents> subscriberManager = new SubscriberManager<>();
    private final String name;

    private OutputStream Logger;

    public Input(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Input{" +
                "mSubscriberManager=" + subscriberManager +
                ", mName='" + name + '\'' +
                ", Logger=" + Logger +
                '}';
    }

    public void setOutput(PerceptronEvents listener) {
        subscriberManager.subscribe(listener);
        subscriberManager.forEach(s -> s.newSubscriptionEvent(this));
    }

    public void fireValueEvent(double value) {
        subscriberManager.forEach(s -> s.valueEvent(this, value));
    }

    public final void fireLearningEvent(double value, double output) {
        subscriberManager.forEach(s -> s.learningEvent(this, output));
    }
}
