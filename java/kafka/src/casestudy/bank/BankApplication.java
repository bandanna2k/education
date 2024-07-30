package casestudy.bank;

import casestudy.bank.handling.DepositWithdrawalHandler;
import casestudy.bank.projections.AccountDao;
import casestudy.bank.publishers.ResponsePublisher;
import casestudy.bank.serde.requests.RequestDeserializer;
import casestudy.bank.serde.requests.RequestSerde;
import casestudy.bank.serde.response.ResponseSerializer;
import education.jackson.requests.Request;
import education.jackson.response.Response;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.ExecConfig;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Driver;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static casestudy.bank.Topics.BOOTSTRAP_SERVERS;
import static casestudy.bank.Topics.REQUESTS_TOPIC;
import static java.util.Collections.singletonList;

public class BankApplication implements Closeable
{
    private static final Driver DRIVER = getDriver();

    private RequestRegistry requestRegistry;
    private ResponsePublisher publisher;

    private KafkaStreams kafkaStreams;
    private AccountDao accountDao;

    void initDatabase(final GenericContainer genericContainer, Optional<BufferedReader> maybeReader) throws IOException, InterruptedException
    {
        genericContainer.setPortBindings(singletonList("13306:3306"));
        genericContainer.start();

        File file = new File("mysqldump.bank.sql");
        if(file.exists())
        {
            if(maybeReader.isPresent())
            {
                System.out.println("Press enter to restore from backup.");
                pause(maybeReader.get());
                mysqlDumpImport(genericContainer);
            }
        }

        String url = "jdbc:mysql://localhost:13306/common?createDatabaseIfNotExist=true";
        Flyway flyway = Flyway.configure()
                .dataSource(url, "root", "password")
                .load();
        flyway.migrate();

        DataSource dataSource = new SimpleDriverDataSource(DRIVER, "jdbc:mysql://localhost:13306", "root", "password");

        accountDao = new AccountDao(dataSource);

        if(maybeReader.isPresent())
        {
            System.out.println("Database migrated. Press enter to start application.");
            pause(maybeReader.get());
        }
    }

    void initKafkaProducer()
    {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ResponseSerializer.class.getName());

        Producer<String, Response> producer;
        producer = new KafkaProducer<>(producerProps);
        publisher = new ResponsePublisher(producer);
    }

    void initBank()
    {
        DepositWithdrawalHandler depositWithdrawalHandler = new DepositWithdrawalHandler(publisher, accountDao);

        requestRegistry = new RequestRegistry();
        requestRegistry.subscribe((RequestRegistry.DepositListener) depositWithdrawalHandler);
        requestRegistry.subscribe((RequestRegistry.WithdrawalListener) depositWithdrawalHandler);
    }

    public void initKafkaConsumer(AtomicBoolean exitApp)
    {
        // Kafka consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RequestDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());

        KafkaConsumer<String, Request> consumer = new KafkaConsumer<>(consumerProps);

        TopicPartition topicPartition = new TopicPartition(REQUESTS_TOPIC, 0);

        consumer.assign(singletonList(topicPartition));
        consumer.seek(topicPartition, 0L);

        System.out.println("Assigned to (Polling) " + REQUESTS_TOPIC);
        while (!exitApp.get())
        {
            ConsumerRecords<String, Request> records = consumer.poll(1000);
            for (ConsumerRecord<String, Request> consumerRecord : records)
            {
                consumerRecord.value().visit(requestRegistry);
            }
        }
        System.out.println("Finished");
    }
    void initKafkaStreams()
    {
        Properties streamProperties = new Properties();
        streamProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        streamProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString());
        streamProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, RequestSerde.class);

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Request> requestStream = streamsBuilder.stream(REQUESTS_TOPIC);

        requestStream.foreach((key, message) -> message.visit(requestRegistry));
//        requestStream.foreach((key, request) ->
//        {
//            request.visit(bankVisitor);
//            System.out.printf("Key: %s, Message: %s%n", key, request);
//        });

        kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamProperties);

        System.out.printf("Listening to topic '%s'%n", REQUESTS_TOPIC);

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook")
        {
            @Override
            public void run()
            {
                kafkaStreams.close();
            }
        });

        kafkaStreams.cleanUp();
        kafkaStreams.setUncaughtExceptionHandler(throwable ->
        {
            throwable.printStackTrace();
            return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
        });
        kafkaStreams.start();
    }

    void startMenuInThread(final BufferedReader reader, AtomicBoolean exitApp)
    {
        new Thread(() -> {
            try
            {
                int menuChoice;
                do
                {
                    System.out.println("Menu");
                    System.out.println("0 - Exit");
                    final String input = reader.readLine();
                    menuChoice = Integer.parseInt(input);

                    switch (menuChoice)
                    {
                        case 0 -> exitApp.set(true);
                    }
                }
                while (!exitApp.get());
            }
            catch(IOException ex)
            {
                System.err.println("Error " + ex.getMessage());
            }
        }).start();
    }

    @Override
    public void close()
    {
        if(kafkaStreams != null) kafkaStreams.close();
    }

    public String pause(BufferedReader reader) throws IOException
    {
        return reader.readLine();
    }

    private static Driver getDriver()
    {
        try
        {
            return (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void mysqlDumpExport(GenericContainer genericContainer) throws IOException, InterruptedException
    {
        ExecConfig execConfig = ExecConfig.builder()
                .command(new String[] {"/usr/bin/mysqldump", "-h127.0.0.1", "-uroot", "-ppassword", "--databases", "common"} )
                .build();
        Container.ExecResult execResult = genericContainer.execInContainer(execConfig);

        Files.writeString(Path.of("mysqldump.bank.sql"), execResult.getStdout());
    }

    public void mysqlDumpImport(GenericContainer genericContainer) throws IOException, InterruptedException
    {
        genericContainer.copyFileToContainer(MountableFile.forHostPath("mysqldump.bank.sql"), "/mysqldump.bank.sql");
        ExecConfig execConfig = ExecConfig.builder()
                .command(new String[] {
                    "/bin/sh", "-c",
                    "/usr/bin/mysql -h127.0.0.1 -uroot -ppassword < /mysqldump.bank.sql"
                })
                .build();
        Container.ExecResult execResult = genericContainer.execInContainer(execConfig);

//        System.out.println(execResult.getStdout());
        System.err.println(execResult.getStderr());
    }
}