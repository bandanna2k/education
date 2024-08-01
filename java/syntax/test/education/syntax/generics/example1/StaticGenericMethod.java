package education.syntax.generics.example1;

abstract public class StaticGenericMethod
{
    public static <T extends GenericInterface> void doSomething(T genericInterface)
    {
        genericInterface.doSomething();
    }

    public static void doSomething2(final GenericInterface genericInterface)
    {
        genericInterface.doSomething();
    }
}
