package education.tests.parameterised.alternative;

import org.junit.Rule;

import java.util.ArrayList;
import java.util.List;

public abstract class AlternativeParameterisedTestBase
{
    @Rule
    public MyProjectTestRule myProjectTestRule = new MyProjectTestRule(data());

    public List<Object[]> data()
    {
        List<Object[]> listTotal = new ArrayList<>();
        listTotal.addAll(extraParams());
        //add your base test data here
        return listTotal;
    }

    protected abstract List<Object[]> extraParams();
}
