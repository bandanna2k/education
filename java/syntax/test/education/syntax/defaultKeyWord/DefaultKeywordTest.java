package education.syntax.defaultKeyWord;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultKeywordTest
{
    @Test
    public void test()
    {
        IMyInterface myInterface = new IMyInterface()
        {
        };
        assertThat(myInterface.getDefaultValue(), equalTo(Integer.MAX_VALUE));
    }
}
