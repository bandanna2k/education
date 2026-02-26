package education.ai.perceptron;

interface PerceptronEvents
{
    void valueEvent(Object sender, double value);
    void newSubscriptionEvent(Object subscriber);
    void learningEvent(Object sender, double output);
}
