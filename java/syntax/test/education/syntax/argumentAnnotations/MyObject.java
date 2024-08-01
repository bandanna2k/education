package education.syntax.argumentAnnotations;

public class MyObject
{
    @Counter int counter = 1;

    public MyObject()
    {
    }

    public void setCounter(int counter)
    {
        this.counter = counter;
    }

    public int getCounter()
    {
        return this.counter;
    }
}
