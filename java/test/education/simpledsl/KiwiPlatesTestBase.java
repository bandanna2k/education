package education.simpledsl;

import com.lmax.simpledsl.api.DslParams;

public class KiwiPlatesTestBase
{
    protected BrowserDsl browser;


    protected static class BrowserDsl
    {
        public void createOrder(final String... args)
        {

        }
    }

    protected BrowserDsl browser(final String name)
    {
        return browser;
    }
}
