package education.takari;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClasspathProperty("education.tests")
public class TakariTest
{
    static
    {
        System.out.println("Takari static constructor");
    }

    public TakariTest()
    {
        System.out.println("Takari constructor");
    }

    @ClasspathSuite.BeforeSuite
    public static void init()
    {
        System.out.println("@ClasspathSuite.BeforeSuite");
    }
}