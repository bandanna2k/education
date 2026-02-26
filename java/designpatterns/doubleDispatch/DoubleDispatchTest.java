package education.designpatterns.doubleDispatch;

import org.junit.Test;

public class DoubleDispatchTest
{
    @Test
    public void shouldDoubleDispatch()
    {
        final Port port = new DisplayPort();
        port.display(new Oval());
        port.display(new Rectangle());
    }
}
