package education.testContainers;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.flywaydb.core.Flyway;
import org.junit.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Driver;
import java.util.List;

import static org.junit.Assert.*;

public class ContainerWithPortMappingTest
{
    @Test
    public void testFixedPortMapping()
    {
        try(final GenericContainer<?> genericContainer = new GenericContainer<>(DockerImageName.parse("nginx:1.27.1"))
                .withCreateContainerCmdModifier(cmd -> cmd
                        .withName("nginx")
                        .withHostConfig(
                            new HostConfig().withPortBindings(
                                new PortBinding(Ports.Binding.bindPort(9980), new ExposedPort(80)))
                ));
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            genericContainer.start();

            // Copy file
            byte[] bytes = this.getClass().getResourceAsStream("/html/index.html").readAllBytes();
            Transferable transferable = Transferable.of(bytes);
            genericContainer.copyFileToContainer(transferable, "/usr/share/nginx/html/index.html");

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