package casestudy.bank;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BankWithPause
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Starting bank (booting up a database container)");

        try(BankApplication bank = new BankApplication();
            GenericContainer genericContainer = new GenericContainer(DockerImageName.parse("mysql:9.0.1"))
                    .withExposedPorts(3306)
                    .withEnv("MYSQL_ROOT_PASSWORD", "password");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            bank.initDatabase(genericContainer);
            bank.pause(reader);
            bank.initKafkaProducer();
            bank.initBank();
            bank.initKafkaStreams();
            bank.initKafkaConsumer();
            bank.startMenu(reader);
        }
    }
}