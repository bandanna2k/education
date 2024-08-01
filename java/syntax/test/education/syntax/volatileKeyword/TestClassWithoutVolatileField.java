package education.syntax.volatileKeyword;

public class TestClassWithoutVolatileField extends TestClass
{
    int i;
    int j;

    void increment() { i++; j++; }

    @Override
    public String toString()
    {
        return ("i:" + i + ",j:" + j);
    }
}
