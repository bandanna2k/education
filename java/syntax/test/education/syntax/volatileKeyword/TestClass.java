package education.syntax.volatileKeyword;

abstract class TestClass
{
    StringBuilder sb;

    TestClass()
    {
        sb = new StringBuilder();
    }

    abstract void increment();
    void append()
    {
        sb.append(this.toString());
        sb.append(System.lineSeparator());
    }
}
