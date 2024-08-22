package education.tests.parameterised.alternative;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AlternativeParameterisedTest extends AlternativeParameterisedTestBase
{
    @Override
    public List<Object[]> extraParams()
    {
        return List.of(
                new Object[] { "TEST1" },
                new Object[] { "TEST2" }
        );
    }

    @Before
    public void setUp()
    {
        Object[] testData = myProjectTestRule.getTestData();
        System.out.println("SETUP:" + testData[0]);
    }

    @Test
    public void test1()
    {
        Object[] testData = myProjectTestRule.getTestData();
        System.out.println("TEST:" + testData[0]);
    }

    @After
    public void tearDown()
    {
        Object[] testData = myProjectTestRule.getTestData();
        System.out.println("TEAR_DOWN:" + testData[0]);
    }
}
