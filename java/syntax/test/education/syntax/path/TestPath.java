package education.syntax.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestPath
{
    @TempDir
    private Path temporaryFolder;

    @Test
    public void testPath() throws IOException
    {
        {
            File file = new File("hello");

            System.out.println("Path:          " + file.getPath());
            System.out.println("Absolute Path: " + file.getAbsolutePath());
        }
        {
            File file = Files.createFile(temporaryFolder.resolve("myfile")).toFile();
            File parent = file.getParentFile();

            System.out.println("Path:          " + file.getPath());
            System.out.println("Absolute Path: " + file.getAbsolutePath());
        }
    }
}
