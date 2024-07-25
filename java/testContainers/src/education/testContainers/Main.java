package education.testContainers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main
{
    public static void main(String[] args)
    {
        new Main().go();
    }

    private void go()
    {
        try(final GenericContainer genericContainer = new GenericContainer(DockerImageName.parse("mysql:9.0.1"))
                .withExposedPorts(3306)
                .withEnv("MYSQL_ROOT_PASSWORD", "password");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            genericContainer.start();

            System.out.println("Press enter.");
            reader.readLine();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        System.out.println("Finished");
    }
}