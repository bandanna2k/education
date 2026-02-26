package education.designpatterns.dispatcher.module2;

import education.designpatterns.dispatcher.module1.CancelOrderInstruction;
import education.designpatterns.dispatcher.module1.PlaceOrderInstruction;
import org.junit.Test;

public class DispatcherTest
{
    @Test
    public void shouldDispatch()
    {
        Dispatcher dispatcher = new Dispatcher(
                new PlaceOrderProcessor(),
                new CancelOrderProcessor()
        );
        dispatcher.process(new PlaceOrderInstruction());
        dispatcher.process(new CancelOrderInstruction());
    }
}
