package casestudy.bank;

import casestudy.bank.projections.Account;
import casestudy.bank.projections.AccountRepository;
import casestudy.bank.serde.requests.RequestSerde;
import education.jackson.requests.Request;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.UUID;

public class Bank
{
    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String REQUESTS_TOPIC = "bank-requests";

    public static void main(String[] args)
    {
        new Bank().go();
    }

    private void go()
    {
        Properties streamProperties = new Properties();
        streamProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        streamProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString());
        streamProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, RequestSerde.class);

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Request> requestStream = streamsBuilder.stream(REQUESTS_TOPIC);

        final AccountRepository accountRepository = new AccountRepository();
        accountRepository.addAccount(new Account(1));
        accountRepository.addAccount(new Account(2));

        BankVisitor bankVisitor = new BankVisitor(accountRepository);
        requestStream.foreach((key, message) -> message.visit(bankVisitor));
//        requestStream.foreach((key, request) ->
//        {
//            request.visit(bankVisitor);
//            System.out.printf("Key: %s, Message: %s%n", key, request);
//        });

        try (KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), streamProperties))
        {
            System.out.printf("Listening to topic '%s'%n", REQUESTS_TOPIC);

            Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook")
            {
                @Override
                public void run()
                {
                    streams.close();
                }
            });

            streams.cleanUp();
            streams.setUncaughtExceptionHandler(throwable ->
            {
                throwable.printStackTrace();
                return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
            });
            streams.start();

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
    }
}