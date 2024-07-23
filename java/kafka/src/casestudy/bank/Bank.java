package casestudy.bank;

import casestudy.bank.projections.Account;
import casestudy.bank.projections.AccountRepository;
import casestudy.bank.serde.MessageSerde;
import education.jackson.Deposit;
import education.jackson.Message;
import education.jackson.Withdrawal;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
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
    public final static String TOPIC = "the-bank";

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
        streamProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, MessageSerde.class);

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Message> messageStream = streamsBuilder.stream(TOPIC);

        final AccountRepository accountRepository = new AccountRepository();
        accountRepository.addAccount(new Account(1));
        accountRepository.addAccount(new Account(2));

        BankVisitor bankVisitor = new BankVisitor(accountRepository);
        messageStream.foreach((key, message) -> message.visit(bankVisitor));
//        messageStream.foreach((key, message) ->
//        {
//            message.visit(bankVisitor);
//            System.out.printf("Key: %s, Message: %s%n", key, message);
//        });

        try (KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), streamProperties))
        {
            System.out.printf("Listening to topic '%s'%n", TOPIC);

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