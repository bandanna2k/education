package education.syntax.generics.example1;

import org.junit.Test;

public class StaticGenericMethodTest
{
    @Test
    public void shouldTestDoSomething()
    {
        final GenericInterface genericInterface = () -> System.out.println("doSomething");
        StaticGenericMethod.doSomething(genericInterface);
    }

    @Test
    public void shouldTestDoSomething2()
    {
        final GenericInterface genericInterface = () -> System.out.println("doSomething");
        StaticGenericMethod.doSomething2(genericInterface);
    }
}
