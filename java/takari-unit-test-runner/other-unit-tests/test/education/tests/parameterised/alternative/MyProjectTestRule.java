package education.tests.parameterised.alternative;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.List;

public class MyProjectTestRule implements TestRule
{
    private final List<Object[]> totalTestData;

    private final int totalTestsSize;

    private int currentTestIndex;

    public MyProjectTestRule(List<Object[]> list)
    {
        this.totalTestsSize = list.size();
        this.totalTestData = list;
    }

    public Object[] getTestData()
    {
        return totalTestData.get(currentTestIndex);
    }

    @Override
    public Statement apply(Statement stmt, Description desc)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                for (int i = 0; i < totalTestsSize; i++)
                {
                    currentTestIndex = i;
                    stmt.evaluate();
                }
            }
        };
    }
}