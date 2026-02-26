package education.designpatterns.doubleDispatch;

public class Rectangle
{
    void displayOn(Port port)
    {
        port.display(this);
    }

    @Override
    public String toString()
    {
        return "Rectangle{}";
    }
}
