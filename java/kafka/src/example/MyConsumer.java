package example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class MyConsumer {

    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String TOPIC = "test-topic";

    private org.apache.kafka.clients.consumer.Consumer<String, String> consumer;

    public static void main(String[] args)
    {
        new MyConsumer().go();
    }

    private void go() {
        initConsumers();

        // Subscribe to the topic
        consumer.subscribe(Collections.singletonList(TOPIC));

        System.out.printf("Subscribed to %s%n", TOPIC);
        while(true) {
            System.out.print(".");
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> consumerRecord : records) {
                System.out.printf("Received message: key=%s value=%s%n", consumerRecord.key(), consumerRecord.value());
            }
        }
    }

    private void initConsumers()
    {
        // Kafka consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(consumerProps);
    }

}