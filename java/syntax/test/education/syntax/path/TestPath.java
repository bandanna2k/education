package education.syntax.path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TestPath
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception
    {
        temporaryFolder.create();
    }

    @Test
    public void testPath() throws IOException
    {
        {
            File file = new File("hello");

            System.out.println("Path:          " + file.getPath());
            System.out.println("Absolute Path: " + file.getAbsolutePath());
        }
        {
            File file = temporaryFolder.newFolder();
            File parent = file.getParentFile();

            System.out.println("Path:          " + file.getPath());
            System.out.println("Absolute Path: " + file.getAbsolutePath());
        }
    }
}
