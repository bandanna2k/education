package education.syntax.construction;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConstructionTest
{
    public class MyObject
    {
        int value = 1;

        public MyObject()
        {
            this.value = 2;
        }

        public int getValue()
        {
            return this.value;
        }
    }
    @Test
    public void testEmptyDoubleBraceInitisation()
    {
        MyObject myObject = new MyObject()
        {
            {
                // This does NOT overwrite the existing constructor,
                //  it appends to the existing constructor.
            }
        };
        assertThat(myObject.getValue(), equalTo(2));
    }
    @Test
    public void testDoubleBraceInitisation()
    {
        MyObject myObject = new MyObject()
        {
            {
                value = 3;
            }
        };
        assertThat(myObject.getValue(), equalTo(3));
    }
}
