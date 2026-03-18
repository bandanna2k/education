package education.simpledsl;

import org.junit.jupiter.api.Test;

public class KiwiPlatesTest extends education.simpledsl.KiwiPlatesTestBase
{
    @Test
    public void shouldCancelOrder()
    {
        browser("session1").createOrder();
        browser("session2").createOrder();
    }
}
