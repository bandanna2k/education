package education.syntax.volatileKeyword;

public class TestClassWithVolatileField extends TestClass
{
    volatile int i;
    volatile int j;

    void increment() { i++; j++; }

    @Override
    public String toString()
    {
        return ("i:" + i + ",j:" + j);
    }
}
