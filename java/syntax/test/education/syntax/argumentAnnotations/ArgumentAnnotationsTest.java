package education.syntax.argumentAnnotations;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ArgumentAnnotationsTest
{
    // TODO

    @Test
    public void testGoodArgumentWithAnnotation()
    {
        MyObject myObject = new MyObject();
        assertThat(myObject.getCounter(), equalTo(1));
    }
}
