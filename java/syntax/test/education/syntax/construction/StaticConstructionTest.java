package education.syntax.construction;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StaticConstructionTest
{
    public class MyObject
    {
        int value = 1;

        {
            value = 2;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "MyObject{" +
                    "value=" + value +
                    '}';
        }
    }
    @Test
    public void defaultConstructorDefinedAsJustBraces()
    {
        final MyObject obj = new MyObject();
        System.out.println(obj);
        assertThat(obj.getValue()).isEqualTo(2);
    }
}
