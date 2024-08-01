package education.syntax.variableArguments;

import org.junit.Test;

import java.util.Arrays;

public class VariableArgumentsTest
{
    @Test
    public void VariableArgumentsValidSyntax()
    {
        out("a", "b" );
        out(new String[] {"b", "c"} );
        //out("a", new String[] {"b", "c"} )
    }
    private static void out(final String... args)
    {
        Arrays.stream(args).forEach(a -> System.out.print(a));
        System.out.println();
    }
}
