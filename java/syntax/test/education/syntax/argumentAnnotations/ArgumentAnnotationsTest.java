package education.syntax.argumentAnnotations;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
