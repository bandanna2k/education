package education.syntax.record;

import org.junit.Test;

public class Records
{
    @Test
    public void testRecord()
    {
        final MyRecord myRecord = new MyRecord("Tom", "Jones");
        System.out.println(myRecord.firstName());
        System.out.println(myRecord.lastName());
        System.out.println(myRecord);
    }

}
