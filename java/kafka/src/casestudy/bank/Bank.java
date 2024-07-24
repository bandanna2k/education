package casestudy.bank;

import casestudy.bank.projections.Account;
import casestudy.bank.projections.AccountRepository;
import casestudy.bank.serde.requests.RequestSerde;
import casestudy.bank.serde.response.ResponseSerializer;
import education.jackson.requests.Request;
import education.jackson.response.Response;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.UUID;

public class Bank implements Closeable
{
    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String REQUESTS_TOPIC = "bank-requests";

    private RequestRegistry requestRegistry;
    private AccountRepository accountRepository;
    private Publisher publisher;
    private KafkaStreams kafkaStreams;

    public static void main(String[] args)
    {
        try(Bank bank = new Bank())
        {
            bank.initKafkaProducer();
            bank.initBank();
            bank.initKafkaStreams();
            bank.startMenu();
        }
    }

    private void initKafkaProducer()
    {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Bank.BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ResponseSerializer.class.getName());

        Producer<String, Response> producer;
        producer = new KafkaProducer<>(producerProps);
        publisher = new Publisher(producer);
    }

    private void initBank()
    {
        accountRepository = new AccountRepository(publisher);
        accountRepository.addAccount(new Account(1));
        accountRepository.addAccount(new Account(2));

        requestRegistry = new RequestRegistry();
        requestRegistry.subscribe((RequestRegistry.DepositListener) accountRepository);
        requestRegistry.subscribe((RequestRegistry.WithdrawalListener) accountRepository);
    }

    private void initKafkaStreams()
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

    private void startMenu()
    {
        int menuChoice;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            do
            {
                System.out.println("Menu");
                System.out.println("1 - Display accounts");
                System.out.println("0 - Exit");
                final String input = reader.readLine();
                menuChoice = Integer.parseInt(input);

                switch (menuChoice)
                {
                    case 1:
                        System.out.println("-- Accounts --");
                        accountRepository.foreach(System.out::println);
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

    @Override
    public void close()
    {
        kafkaStreams.close();
    }
}