package education.designpatterns.doubleDispatch;

public class Oval
{
    void displayOn(Port port)
    {
        port.display(this);
    }

    @Override
    public String toString()
    {
        return "Oval{}";
    }
}
