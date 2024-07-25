package education.testContainers;

import org.flywaydb.core.Flyway;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Driver;

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
            String url = STR."jdbc:mysql://localhost:\{mysqlPort}/common?createDatabaseIfNotExist=true";
            Flyway flyway = Flyway.configure()
                    .dataSource(url, "root", "password")
                    .load();
            flyway.migrate();

            Driver driver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
            DataSource dataSource = new SimpleDriverDataSource(driver, STR."jdbc:mysql://localhost:\{mysqlPort}/", "root", "password");
            PropertyDao dao = new PropertyDao(dataSource);
            String offset = dao.getProperty("kafka.offset");
            System.out.printf("Offset: '%s'%n", offset);

            System.out.println("Press enter.");
            reader.readLine();
        }
        catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        System.out.println("Finished");
    }
}