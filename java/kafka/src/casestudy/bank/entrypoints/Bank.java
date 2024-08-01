package casestudy.bank.entrypoints;

import casestudy.bank.BankApplication;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Optional.empty;

public class Bank
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        System.out.println("Starting bank (booting up a database container)");
        final AtomicBoolean exitApp = new AtomicBoolean(false);

        try(BankApplication bank = new BankApplication();
            GenericContainer genericContainer = new GenericContainer(DockerImageName.parse("mysql:9.0.1"))
                    .withExposedPorts(3306)
                    .withEnv("MYSQL_ROOT_PASSWORD", "password");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            bank.initDatabase(genericContainer, empty());
            bank.initKafkaProducer();
            bank.initBank();
//            bank.initKafkaStreams();

            bank.startMenuInThread(reader, exitApp);
            bank.initKafkaConsumer(exitApp);
        }
    }
}