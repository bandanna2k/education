package education.simpledsl;

import org.junit.Test;

public class KiwiPlatesTest extends KiwiPlatesTestBase
{
    @Test
    public void shouldCancelOrder()
    {
        browser("session1").createOrder();


        browser("session2").createOrder();
    }
}
