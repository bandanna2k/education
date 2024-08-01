package education.ai.perceptron;

import utilities.StringGenerator;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class Perceptron extends Input implements PerceptronEvents
{
    private final Random random;
    private final SubscriberManager<PerceptronEvents> subscriberManagerOfPerceptrons;
    private final Map<Object, Input> mapOfInputs;

    double weight;
    OptionalDouble currentValue = OptionalDouble.empty();
    OptionalDouble learningValue = OptionalDouble.empty();
    private final ActivationFunction activationFunction;
    private final double learningRate;
    private double bias;

    public Perceptron(Random random, double learningRate, ActivationFunction activationFunction)
    {
        super(new StringGenerator(random).randomString(8));

        this.random = random;
        this.learningRate = learningRate;
        this.activationFunction = activationFunction;

        subscriberManagerOfPerceptrons = new SubscriberManager<>();
        mapOfInputs = new HashMap<>();
    }

    public void valueEvent(Object sender, double value)
    {
        Input inputRecord = mapOfInputs.get(sender);
        if (inputRecord == null)
        {
            throw new RuntimeException("Input not found.");
        }

        if(inputRecord instanceof Perceptron)
        {
            Perceptron perceptron = (Perceptron)inputRecord;
            if (perceptron.currentValue.isPresent())
            {
                throw new RuntimeException("Cannot set value. Value already exists.");
            }

            perceptron.currentValue = OptionalDouble.of(value);
            //  Display value
            this.logPerceptron();
            //  Check all inputs for values
            if (this.DoWeHaveAllInputs())
            {
                double sumOfAllValuesAndWeights = this.getSumOfAllCurrentValuesAndWeights();
                double output = activate(sumOfAllValuesAndWeights);

                clearAllCurrentValues();
                fireValueEvent(output);
            }
        }
    }

    private double activate(double input)
    {
//        return switch (this.activationFunction)
//                {
//                    case BooleanFunction -> input > 0 ? 1 : 0;
//                    case SigmoidFunction -> Sigmoid.Sigmoid(input);
//                };
        return 0;
    }

    private void logPerceptron()
    {
//        this.Log(String.format("-- Perceptron ID:%d --", this.hashCode()));
//        for (Map.Entry<Object, PerceptronInput> item : mapOfInputs.entrySet())
//        {
//            this.Log(String.format("-- Input ID:{0}, Weight:{1:f4} {1}, Current Value:{2}, Learning Value:{3} --",
//                    item.getKey(), item.getValue().weight, item.getValue().currentValue, item.getValue().learningValue));
//        }
    }

    public void learningEvent(Object sender, double output)
    {
        Perceptron inputRecord = (Perceptron)mapOfInputs.get(sender);
        //  TODO Null check
        inputRecord.learningValue = OptionalDouble.of(output);

        //  Check all learning values
        this.checkIfAllLearningValuesEqual();

        //  Display value
        this.logPerceptron();

        //  Check if we have all learning values
        if (this.doWeHaveAllLearningValues())
        {
            double sumOfAllValuesAndWeights = this.getSumOfAllCurrentValuesAndWeights();
            double feedForwardOutput = this.activate(sumOfAllValuesAndWeights);
            double error = (output - feedForwardOutput);
            for (Perceptron item : getPerceptronInputs())
            {
                item.weight = (item.weight
                        + (this.learningRate
                        * (error * inputRecord.learningValue.getAsDouble())));
            }

            //  Clear all learning values
            this.clearAllLearningValues();

            //  Fire learning event
            this.fireLearningEvent(output);
        }
    }

    private void checkIfAllLearningValuesEqual()
    {
        //  TODO Implement this
    }

    private double getSumOfAllCurrentValuesAndWeights()
    {
        double total = 0;
        for (Perceptron inputRecord : getPerceptronInputs())
        {
            if (inputRecord.currentValue.isPresent())
            {
                total = (total + (inputRecord.weight * inputRecord.currentValue.getAsDouble()));
            }
        }
        return total + bias;
    }

    private double GetSumOfAllLearningValueAndWeights()
    {
        double total = 0;
        for (Perceptron inputRecord : getPerceptronInputs())
        {
            if (inputRecord.learningValue.isPresent())
            {
                total = (total + (inputRecord.weight * inputRecord.learningValue.getAsDouble()));
            }
        }
        return total;
    }

    private void clearAllCurrentValues()
    {
        getPerceptronInputs().forEach(p -> p.currentValue = OptionalDouble.empty());
    }

    private void clearAllLearningValues()
    {
        getPerceptronInputs().forEach(p -> p.learningValue = OptionalDouble.empty());
    }

    private boolean DoWeHaveAllInputs()
    {
        for (Perceptron inputRecord : getPerceptronInputs())
        {
            if (inputRecord.currentValue.isPresent())
            {
                //  Continue
            }
            else
            {
                return false;
            }

        }

        //  Return true if all inputs have a value
        return true;
    }

    private boolean DoAnyInputsHaveAValue()
    {
        for (Perceptron inputRecord : getPerceptronInputs())
        {
            if (inputRecord.currentValue.isPresent())
            {
                return true;
            }

        }

        //  Return false if a value is not found
        return false;
    }

    private boolean doWeHaveAllLearningValues()
    {
        for (Perceptron inputRecord : getPerceptronInputs())
        {
            if (inputRecord.learningValue.isPresent())
            {
                //  Continue
            }
            else
            {
                return false;
            }

        }

        //  Return true if all inputs have a value
        return true;
    }

    private void Log(String text)
    {
//            if (this.Logger != null)
//                this.Logger.WriteLine(text);
    }

    public void fireValueEvent(double output)
    {
        this.Log(String.format("Output fired:{0}", output));
        subscriberManagerOfPerceptrons.forEach(s -> s.valueEvent(this, output));
    }

    protected void fireLearningEvent(double output)
    {
        this.Log(String.format("Learning Output fired:{0}", output));
        subscriberManagerOfPerceptrons.forEach(s -> s.learningEvent(this, output));
    }

    public void setOutput(PerceptronEvents listener)
    {
        subscriberManagerOfPerceptrons.subscribe(listener);
        subscriberManagerOfPerceptrons.forEach(s -> s.newSubscriptionEvent(this));
    }

    public void newSubscriptionEvent(Object subscriber)
    {
        Perceptron inputRecord = new PerceptronInputBuilder(random).build();
        inputRecord.weight = Weight.randomWeight(random);
        mapOfInputs.put(subscriber, inputRecord);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (Perceptron input : getPerceptronInputs())
        {
            sb.append(input).append(" W:").append(input.toStringWeight()).append(lineSeparator());
        }
        if(!mapOfInputs.isEmpty())
        {
            sb.append(" B:").append(bias).append(lineSeparator());
            sb.append("----------").append(lineSeparator());
        }
        return sb.toString();
    }

    private Collection<Perceptron> getPerceptronInputs()
    {
        return mapOfInputs.values().stream()
                .filter(input -> input instanceof Perceptron)
                .map(input -> (Perceptron)input).collect(Collectors.toList());
    }

    private String toStringWeight()
    {
        return String.format("%.4f", weight);
    }

    public void setBias(final double bias)
    {
        this.bias = bias;
    }

    public void setWeights()
    {
    }
}
