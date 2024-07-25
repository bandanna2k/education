package education.testContainers;

import com.github.dockerjava.api.model.VolumesFrom;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

import static java.util.Collections.*;

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

            int mysqlPort = genericContainer.getMappedPort(3306);
            String url = STR."jdbc:mysql://localhost:\{mysqlPort}/company?createDatabaseIfNotExist=true";
            Flyway flyway = Flyway.configure()
                    .dataSource(url, "root", "password")
                    .load();
            flyway.migrate();

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