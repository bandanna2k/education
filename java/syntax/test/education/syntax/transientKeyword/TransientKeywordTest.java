package education.syntax.transientKeyword;

import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TransientKeywordTest
{
    @Test
    public void shouldNotBeAbleToReadWriteTransientFieldsToFile() throws IOException, ClassNotFoundException
    {
        File tempFile = File.createTempFile("transientTest","");

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        // Write to buffer
        try
        {
            MyTestClass mtc = new MyTestClass();
            mtc.setValues();

            fos = new FileOutputStream(tempFile);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(mtc);
        }
        finally
        {
            oos.close();
            fos.close();
        }

        // Read from buffer
        try
        {
            fis = new FileInputStream(tempFile);
            ois = new ObjectInputStream(fis);

            Object o = ois.readObject();
            MyTestClass mtc = (MyTestClass)o;

            assertThat(mtc.A,is(equalTo("A")));
            assertThat(mtc.B,is(equalTo("B")));
            assertThat(mtc.C,is(nullValue()));
            assertThat(mtc.D,is(nullValue()));
        }
        finally
        {
            ois.close();
            fis.close();
        }
    }

    @Test
    public void shouldNotBeAbleToReadWriteTransientFieldsFromMemory() throws IOException, ClassNotFoundException
    {
        byte[] buffer = null;

        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;

        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

        // Write to buffer
        try
        {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            MyTestClass mtc = new MyTestClass();
            mtc.setValues();

            oos.writeObject(mtc);

            buffer = baos.toByteArray();
        }
        finally
        {
            oos.close();
            baos.close();
        }

        // Read from buffer
        try
        {
            bais = new ByteArrayInputStream(buffer);
            ois = new ObjectInputStream(bais);

            Object o = ois.readObject();
            MyTestClass mtc = (MyTestClass)o;

            assertThat(mtc.A,is(equalTo("A")));
            assertThat(mtc.B,is(equalTo("B")));
            assertThat(mtc.C,is(nullValue()));
            assertThat(mtc.D,is(nullValue()));
        }
        finally
        {
            if(ois!=null) ois.close();
            if(bais!=null) bais.close();
        }
    }
}
