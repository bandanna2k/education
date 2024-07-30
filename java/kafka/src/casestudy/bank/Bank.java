package casestudy.bank;

import org.testcontainers.containers.Container;
import org.testcontainers.containers.ExecConfig;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

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
            bank.initDatabase(genericContainer);
            bank.initKafkaProducer();
            bank.initBank();
//            bank.initKafkaStreams();

            bank.startMenu(reader, exitApp);
            bank.initKafkaConsumer(exitApp);

            mysqlDump(genericContainer);
        }
    }

    private static void mysqlDump(GenericContainer genericContainer) throws IOException, InterruptedException
    {
        ExecConfig execConfig = ExecConfig.builder()
                .command(new String[] {"/usr/bin/mysqldump", "-h127.0.0.1", "-uroot", "-ppassword", "--databases", "common"} )
                .build();
        Container.ExecResult execResult = genericContainer.execInContainer(execConfig);

        Files.writeString(Path.of("mysqldump.bank.sql"), execResult.getStdout());
    }
}