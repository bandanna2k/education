package education.designpatterns.doubleDispatch;

public interface Port
{
    void display(final Rectangle rectangle);

    void display(final Oval oval);
}
