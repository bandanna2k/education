package education.ai.perceptron;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SubscriberManager<T>  {

    private final List<T> listOfSubscribers;

    public SubscriberManager() {
        listOfSubscribers = new ArrayList<T>();
    }

    public void subscribe(T listener) {
        listOfSubscribers.add(listener);
    }

    public void Unsubscribe(T listener) {
        listOfSubscribers.remove(listener);
    }

    public void forEach(Consumer<T> consumer)
    {
        listOfSubscribers.forEach(consumer);
    }
}