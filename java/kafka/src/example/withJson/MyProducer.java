package example.withJson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

public class MyProducer {

    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String TOPIC = "test-topic";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Producer<String, JsonNode> producer;

    public static void main(String[] args)
    {
        new MyProducer().go();
    }

    private void go() {
        initProducers();

        // Consume messages from the topic
        int menuChoice;
        try(final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            do {
                System.out.println("Menu");
                System.out.println("1 - Add event");
                System.out.println("2 - Add lots of events");
                System.out.println("0 - Exit");
                final String input = reader.readLine();
                menuChoice = Integer.parseInt(input);

                switch (menuChoice)
                {
                    case 1:
                        addEvent();
                        break;
                    case 2:
                        for (int i = 0; i < 10; i++) {
                            addEvent();
                        }
                        break;
                }
            }
            while (menuChoice > 0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addEvent() {
        final Event event = new Event("event", "value-" + new Date());
        System.out.println("Adding event: " + event);
        final JsonNode jsonNode = objectMapper.valueToTree(event);
        ProducerRecord<String, JsonNode> record = new ProducerRecord<>(TOPIC, "test-key", jsonNode);
        producer.send(record);
    }

    private void initProducers()
    {
        // Kafka producer configuration
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        producer = new KafkaProducer<>(producerProps);
    }
}