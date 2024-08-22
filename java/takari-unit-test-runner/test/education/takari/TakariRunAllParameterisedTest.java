package education.takari;

import education.tests.parameterised.alternative.AlternativeParameterisedTestBase;
import education.tests.parameterised.alternative.MyProjectTestRule;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClasspathProperty("education.tests")
@Deprecated(since = "Didin't work")
class TakariRunAllParameterisedTest extends AlternativeParameterisedTestBase
{
    @Override
    public List<Object[]> extraParams()
    {
        return List.of(
                new Object[] { "Takari1" },
                new Object[] { "Takari2" }
        );
    }
}