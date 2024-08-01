package education.syntax.interfaces;

import org.junit.Test;

public class InterfacesWithLambda
{
    @Test
    public void shouldTestLambdas()
    {
        final InterfaceWith1Method interface1 = () -> System.out.println("method1");
        final InterfaceWith2Methods interface2 = new InterfaceWith2Methods()
        {
            @Override
            public void method1()
            {
                System.out.println("method1");
            }
            @Override
            public void method2()
            {
                System.out.println("method2");
            }
        };
        interface1.method1();
        interface2.method1();
        interface2.method2();
    }

    interface InterfaceWith1Method
    {
        void method1();
    }
    interface InterfaceWith2Methods
    {
        void method1();
        void method2();
    }
}
