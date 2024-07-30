package casestudy.bank;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class BankWithPause
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        System.out.println("Starting bank (booting up a database container)");

        AtomicBoolean exitApp = new AtomicBoolean(false);
        try(BankApplication bank = new BankApplication();
            GenericContainer genericContainer = new GenericContainer(DockerImageName.parse("mysql:9.0.1"))
                    .withExposedPorts(3306)
                    .withEnv("MYSQL_ROOT_PASSWORD", "password");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            bank.initDatabase(genericContainer, Optional.of(reader));
            bank.initKafkaProducer();
            bank.initBank();
//            bank.initKafkaStreams();
            bank.startMenuInThread(reader, exitApp);
            bank.initKafkaConsumer(exitApp);

            bank.mysqlDumpExport(genericContainer);
        }
    }
}