package education.syntax.systemout;

import org.junit.Ignore;
import org.junit.Test;

public class SystemOutTest
{
    public static void main(String[] args) throws InterruptedException
    {
        for (int i = 0; i < 100; i++)
        {
            System.out.print("Going up " + i);
            Thread.sleep(100);
            System.out.print("\r");
        }
        System.out.printf("%nFinished.");
    }

    @Test
    @Ignore // Does not work with test and intellij
    public void testOverwritingPrintedLine() throws InterruptedException
    {
        for (int i = 0; i < 100; i++)
        {
            System.out.print("Going up" + i);
            System.out.flush();
            Thread.sleep(100);
//            System.out.print("\r");
        }
        System.out.printf("%nFinished.");
    }
}
