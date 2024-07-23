package example.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class MyConsumer {

    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String TOPIC = "test-topic";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private org.apache.kafka.clients.consumer.Consumer<String, JsonNode> consumer;

    public static void main(String[] args) {
        new MyConsumer().go();
    }

    private void go() {
        initConsumers();

        // Subscribe to the topic
        consumer.subscribe(Collections.singletonList(TOPIC));

        System.out.printf("Subscribed to %s%n", TOPIC);
        try {
            while (true) {
                System.out.print(".");
                ConsumerRecords<String, JsonNode> records = consumer.poll(1000);
                for (ConsumerRecord<String, JsonNode> consumerRecord : records) {
                    Event event = objectMapper.treeToValue(consumerRecord.value(), Event.class);
                    System.out.printf("Received message: key=%s value=%s%n", consumerRecord.key(), consumerRecord.value());
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initConsumers() {
        // Kafka consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());

        consumer = new KafkaConsumer<>(consumerProps);
    }

}