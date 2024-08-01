package education.syntax.temporaryfiles;

import java.io.*;
import java.util.Random;

public abstract class TemporaryFiles
{
    public static File createTempFile(final String prefix, final String suffix) throws IOException
    {
        final File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static File createTempFile(final String prefix, final String suffix, final File dir) throws IOException
    {
        final File tempFile = File.createTempFile(prefix, suffix, dir);
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static File createTempFileWithRandomData(final String prefix, final String suffix, final int length, final Random random) throws IOException
    {
        final File file = createTempFile(prefix, suffix);
        try(final Writer writer = new BufferedWriter(new FileWriter(file)))
        {
            for (int i = 0; i < length; i++)
            {
                writer.write(random.nextInt());
            }
        }
        return file;
    }
}
