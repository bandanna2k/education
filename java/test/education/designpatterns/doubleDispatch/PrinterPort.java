package education.designpatterns.doubleDispatch;

public class PrinterPort implements Port
{
    @Override
    public void display(final Rectangle rectangle)
    {
        System.out.println("PrinterPort:" + rectangle);
    }

    @Override
    public void display(final Oval oval)
    {
        System.out.println("PrinterPort:" + oval);
    }
}
