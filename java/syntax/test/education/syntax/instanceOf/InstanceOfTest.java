package education.syntax.instanceOf;

import org.junit.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class InstanceOfTest
{
    @Test
    public void testInstanceOf()
    {
        MyBaseObject object = new MyObject();
        if(object instanceof MyObject)
        {
            assertThat(((MyObject)object).me).isEqualTo(2);
        }
    }

    @Test
    public void testInstanceOfWrapper()
    {
        {
            MyBaseObject object = new MyObject();
            ifInstanceOf(object, MyObject.class, obj -> obj.me = 3);
            assertThat(object.toString()).contains("3");
        }
        {
            MyBaseObject object = new MyObject();
            ifInstanceOf(object, Long.class, _ -> fail());
        }
    }

    private static <T> void ifInstanceOf(Object object, Class<T> clazz, Consumer<T> consumer)
    {
        if(clazz.isInstance(object))
        {
            consumer.accept(clazz.cast(object));
        }
    }

    private static class MyObject extends MyBaseObject
    {
        int me = 2;

        @Override
        public String toString()
        {
            return "MyObject{" +
                    "me=" + me +
                    "} " + super.toString();
        }
    }
    private static class MyBaseObject
    {
        int base = 1;

        @Override
        public String toString()
        {
            return "MyBaseObject{" +
                    "base=" + base +
                    '}';
        }
    }
}
