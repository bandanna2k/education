package casestudy.bank.projections;

import org.flywaydb.core.Flyway;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Driver;

import static java.util.Collections.singletonList;

public class DaoTest
{
    private static GenericContainer genericContainer;
    private static DataSource dataSource;

    protected void clearTable(String tableName)
    {
        JdbcOperations jdbc = new JdbcTemplate(getDataSource());
        jdbc.execute(STR."delete from \{tableName}");
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        genericContainer.stop();
    }

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        Driver driver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
        genericContainer = new GenericContainer(DockerImageName.parse("mysql:9.0.1"))
                .withExposedPorts(3306)
                .withEnv("MYSQL_ROOT_PASSWORD", "password");
        genericContainer.setPortBindings(singletonList("13306:3306"));
        genericContainer.start();

        String url = "jdbc:mysql://localhost:13306/common?createDatabaseIfNotExist=true";
        Flyway flyway = Flyway.configure()
                .dataSource(url, "root", "password")
                .load();
        flyway.migrate();

        dataSource = new SimpleDriverDataSource(driver, "jdbc:mysql://localhost:13306", "root", "password");
    }

    public static DataSource getDataSource()
    {
        return dataSource;
    }
}
