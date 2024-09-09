package education.testContainers;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.DockerImageName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class MySqlContainerTest
{
    @Test
    public void testMySql()
    {
        double start = System.currentTimeMillis();
        try(final GenericContainer<?> genericContainer = new GenericContainer<>(DockerImageName.parse("mysql"))
//                .withTmpFs(Map.of("/var/lib/mysql", "rw"))
                .withCreateContainerCmdModifier(cmd -> cmd
                        .withEnv("MYSQL_ROOT_PASSWORD=mysql")
                        .withName("mysql")
                        .withHostConfig(
                                new HostConfig().withPortBindings(
                                        new PortBinding(Ports.Binding.bindPort(3306), new ExposedPort(3306))))
                );
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            genericContainer.start();

            double duration = System.currentTimeMillis() - start;
            System.out.printf("Duration: %.2f%n", duration / 1000);

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