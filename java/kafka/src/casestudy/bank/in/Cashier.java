package casestudy.bank.in;

import casestudy.bank.Bank;
import casestudy.bank.serde.MessageSerializer;
import education.jackson.Deposit;
import education.jackson.Message;
import education.jackson.Withdrawal;
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

public class Cashier
{
    private final Random random = new Random(1);

    private Producer<String, Message> producer;

    public static void main(String[] args)
    {
        new Cashier().go();
    }

    private void go()
    {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Bank.BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class.getName());
        producer = new KafkaProducer<>(producerProps);

        int menuChoice;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            do
            {
                System.out.println("Menu");
                System.out.println("1 - Add deposit");
                System.out.println("2 - Add withdrawal");
                System.out.println("0 - Exit");
                final String input = reader.readLine();
                menuChoice = Integer.parseInt(input);

                switch (menuChoice)
                {
                    case 1:
                        addEvent(new Deposit(String.valueOf(random.nextDouble())));
                        break;
                    case 2:
                        addEvent(new Withdrawal(String.valueOf(random.nextDouble())));
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

    private void addEvent(Message message)
    {
        ProducerRecord<String, Message> record = new ProducerRecord<>(Bank.TOPIC, getKey(), message);
        producer.send(record);
        producer.flush();
        System.out.println("Message sent: " + message);
    }

    private String getKey()
    {
        return String.valueOf(System.currentTimeMillis());
    }
}
