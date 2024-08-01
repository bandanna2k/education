package education.syntax.volatileKeyword;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VolatileKeywordTest extends TestClassWithVolatileField {
    // TODO

    @Test
    public void testWithVolatile() throws InterruptedException
    {
        TestClass testClass = new TestClassWithVolatileField();
        test(testClass);
    }
    @Test
    public void testWithoutVolatile() throws InterruptedException
    {
        TestClass testClass = new TestClassWithoutVolatileField();
        test(testClass);
    }
    private void test(TestClass testClass) throws InterruptedException
    {
        List<Thread> listOfThreads = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    for (int i = 0; i < 100; i++)
                    {
                        testClass.increment();
                        testClass.append();
                    }
                    System.out.println(testClass.sb.toString());
                }
            });
            listOfThreads.add(thread);
        }
        for (Thread thread : listOfThreads) thread.start();
        for (Thread thread : listOfThreads) thread.join();
    }
}
