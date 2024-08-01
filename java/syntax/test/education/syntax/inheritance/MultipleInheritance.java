package education.syntax.inheritance;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MultipleInheritance implements InterfaceA//, InterfaceB
{
    @Test
    public void canIHaveMultipleInheritenceInJava()
    {
        MultipleInheritance mi = new MultipleInheritance();

        assertThat(mi.myFunction(),is(equalTo(1)));
    }
}
