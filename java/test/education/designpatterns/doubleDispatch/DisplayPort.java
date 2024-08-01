package education.designpatterns.doubleDispatch;

public class DisplayPort implements Port
{
    @Override
    public void display(final Rectangle rectangle)
    {
        System.out.println("DisplayPort:" + rectangle);
    }

    @Override
    public void display(final Oval oval)
    {
        System.out.println("DisplayPort:" + oval);
    }
}
