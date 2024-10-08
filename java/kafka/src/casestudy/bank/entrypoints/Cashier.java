package casestudy.bank.entrypoints;

import casestudy.bank.projections.AccountDao;
import casestudy.bank.publishers.AsyncExecutor;
import casestudy.bank.publishers.RequestPublisher;
import casestudy.bank.publishers.ResponsePublisher;
import casestudy.bank.serde.requests.RequestSerializer;
import casestudy.bank.serde.response.ResponseSerde;
import casestudy.bank.serde.response.ResponseSerializer;
import casestudy.bank.vertx.CashierVerticle;
import education.jackson.requests.Deposit;
import education.jackson.requests.Request;
import education.jackson.requests.Withdrawal;
import education.jackson.response.Balance;
import education.jackson.response.Error;
import education.jackson.response.Response;
import education.jackson.response.ResponseVisitor;
import io.vertx.core.Vertx;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Driver;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import static casestudy.bank.Topics.BOOTSTRAP_SERVERS;
import static casestudy.bank.Topics.RESPONSE_TOPIC;
import static java.util.UUID.randomUUID;

public class Cashier implements Closeable
{
    private final Random random = new Random(1);

    private KafkaStreams kafkaStreams;
    private RequestPublisher requestPublisher;
    private AsyncExecutor executor;
    private Vertx vertx;
    private AccountDao accountDao;

    public static void main(String[] args)
    {
        try(final Cashier cashier = new Cashier())
        {
            cashier.initDataSource();
            cashier.initKafkaProducers();
            cashier.initVertx();
            cashier.initKafkaStreams();
            cashier.startMenu();
        }
    }

    public Cashier()
    {
        vertx = Vertx.vertx();
    }

    private void initDataSource()
    {
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

    private void initKafkaProducers()
    {
        requestPublisher = new RequestPublisher(getRequestProducer());
        executor = new AsyncExecutor(vertx, new ResponsePublisher(getResponseProducer()));
    }
    private static KafkaProducer<String, Request> getRequestProducer()
    {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, RequestSerializer.class.getName());
        return new KafkaProducer<>(producerProps);
    }
    private static KafkaProducer<String, Response> getResponseProducer()
    {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ResponseSerializer.class.getName());
        return new KafkaProducer<>(producerProps);
    }

    private void initVertx()
    {
        vertx.deployVerticle(new CashierVerticle(vertx, requestPublisher, executor, accountDao))
                .onSuccess(event -> System.out.println("Verticles deployed."))
                .onFailure(event -> System.err.println("Failed to deploy. " + event.getMessage()));
    }

    private void initKafkaStreams()
    {
        Properties streamProperties = new Properties();
        streamProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        streamProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString());
        streamProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, ResponseSerde.class);

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Response> requestStream = streamsBuilder.stream(RESPONSE_TOPIC);

        requestStream.foreach((key, message) -> message.visit(new ResponseVisitor()
        {
            @Override
            public void visit(final Balance balance)
            {
                executor.onResponseReceived(balance);
            }

            @Override
            public void visit(final Error error)
            {
                executor.onResponseReceived(error);
            }
        }));
//        requestStream.foreach((key, request) ->
//        {
//            request.visit(bankVisitor);
//            System.out.printf("Key: %s, Message: %s%n", key, request);
//        });

        kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamProperties);

        System.out.printf("Listening to topic '%s'%n", RESPONSE_TOPIC);

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

    private void startMenu()
    {
        int menuChoice;
        int accountId = 1;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            do
            {
                System.out.println("0 - Exit");
                final String input = reader.readLine();
                menuChoice = Integer.parseInt(input);

                switch (menuChoice)
                {
                    case 1:
                        addEvent(new Deposit(randomUUID(), accountId, String.valueOf(random.nextDouble())));
                        break;
                    case 2:
                        addEvent(new Withdrawal(randomUUID(), accountId, String.valueOf(random.nextDouble())));
                        break;
                }
            }
            while (menuChoice > 0);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void addEvent(Request request)
    {
        requestPublisher.publishRequest(request);
        System.out.println("Direct request sent: " + request);
    }

    @Override
    public void close()
    {
        kafkaStreams.close();
        vertx.close();
    }
}
