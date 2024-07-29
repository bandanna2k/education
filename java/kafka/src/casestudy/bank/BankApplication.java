package casestudy.bank;

import casestudy.bank.handling.DepositWithdrawalHandler;
import casestudy.bank.projections.AccountDao;
import casestudy.bank.publishers.ResponsePublisher;
import casestudy.bank.serde.requests.RequestDeserializer;
import casestudy.bank.serde.requests.RequestSerde;
import casestudy.bank.serde.response.ResponseSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import education.jackson.requests.Request;
import education.jackson.response.Response;
import example.withJson.Event;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Driver;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import static casestudy.bank.Topics.BOOTSTRAP_SERVERS;
import static casestudy.bank.Topics.REQUESTS_TOPIC;
import static java.util.Collections.singletonList;

public class BankApplication implements Closeable
{
    private RequestRegistry requestRegistry;
    private DepositWithdrawalHandler depositWithdrawalHandler;
    private ResponsePublisher publisher;

    private KafkaStreams kafkaStreams;
    private AccountDao accountDao;
    private boolean okToConsumer = true;

    void initDatabase(final GenericContainer genericContainer) throws IOException
    {
        genericContainer.setPortBindings(singletonList("13306:3306"));
        genericContainer.start();

        String url = "jdbc:mysql://localhost:13306/common?createDatabaseIfNotExist=true";
        Flyway flyway = Flyway.configure()
                .dataSource(url, "root", "password")
                .load();
        flyway.migrate();

        try
        {
            Driver driver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
            DataSource dataSource = new SimpleDriverDataSource(driver, "jdbc:mysql://localhost:13306", "root", "password");

            accountDao = new AccountDao(dataSource);
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
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
        depositWithdrawalHandler = new DepositWithdrawalHandler(publisher, accountDao);

        requestRegistry = new RequestRegistry();
        requestRegistry.subscribe((RequestRegistry.DepositListener) depositWithdrawalHandler);
        requestRegistry.subscribe((RequestRegistry.WithdrawalListener) depositWithdrawalHandler);
    }

    public void initKafkaConsumer()
    {
        // Kafka consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RequestDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());

        KafkaConsumer<String, Request> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(REQUESTS_TOPIC));
//        System.out.printf("Consuming %n%s%n", consumer.listTopics().entrySet().stream()
//                .map(entry -> entry.getKey() + " " + entry.getValue()).collect(Collectors.joining("\n")));
        System.out.println("Subscribed to " + REQUESTS_TOPIC);
        while (okToConsumer)
        {
            ConsumerRecords<String, Request> records = consumer.poll(1000);
            for (ConsumerRecord<String, Request> consumerRecord : records)
            {
                consumerRecord.value().visit(requestRegistry);
            }
        }
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

    void startMenu(final BufferedReader reader) throws IOException
    {
        int menuChoice;
        do
        {
            System.out.println("Menu");
//            System.out.println("1 - Display accounts");
            System.out.println("0 - Exit");
            final String input = reader.readLine();
            menuChoice = Integer.parseInt(input);

//            switch (menuChoice)
//            {
//                case 1:
//                    System.out.println("-- Accounts --");
//                    accountHandler.foreach(System.out::println);
//                    break;
//            }
        }
        while (menuChoice > 0);
    }

    @Override
    public void close()
    {
        if(kafkaStreams != null) kafkaStreams.close();
    }

    public void pause(BufferedReader reader) throws IOException
    {
        System.out.println("Open bank (press enter)");
        final String input = reader.readLine();
    }
}