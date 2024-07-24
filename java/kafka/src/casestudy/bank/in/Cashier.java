package casestudy.bank.in;

import casestudy.bank.Bank;
import casestudy.bank.serde.requests.RequestSerde;
import casestudy.bank.serde.requests.RequestSerializer;
import education.jackson.requests.Deposit;
import education.jackson.requests.Request;
import education.jackson.requests.Withdrawal;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Random;

import static java.util.UUID.randomUUID;

public class Cashier
{
    private final Random random = new Random(1);

    private Producer<String, Request> producer;

    public static void main(String[] args)
    {
        new Cashier().go();
    }

    private void go()
    {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Bank.BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, RequestSerializer.class.getName());
        producer = new KafkaProducer<>(producerProps);

        int menuChoice;
        int accountId = 1;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            do
            {
                System.out.println("Menu");
                System.out.println("1 - Add deposit for " + accountId);
                System.out.println("2 - Add withdrawal for " + accountId);
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
                accountId = accountId == 1 ? 2 : 1;
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
        ProducerRecord<String, Request> record = new ProducerRecord<>(Bank.REQUESTS_TOPIC, request);
        producer.send(record);
        producer.flush();
        System.out.println("Request sent: " + request);
    }
}
