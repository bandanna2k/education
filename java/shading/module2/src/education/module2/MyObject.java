package education.module2;

public class MyObject
{
    public static void main()
    {
        System.out.println(new MyObject());
    }

    @Override
    public String toString()
    {
        return MyObject.class.getName();
    }
}
