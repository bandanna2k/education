package education.simpledsl;

public class KiwiPlatesTestBase
{
    protected BrowserDsl browser = new BrowserDsl();


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
