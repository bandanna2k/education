package casestudy.bank.eventvalidator;

import casestudy.bank.serde.response.ResponseDeserializer;
import education.jackson.response.Response;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static casestudy.bank.Topics.BOOTSTRAP_SERVERS;
import static casestudy.bank.Topics.RESPONSE_TOPIC;
import static java.util.Collections.singletonList;

public class ResponseValidator
{
    public static void main(String[] args)
    {
        new ResponseValidator().go();
    }

    private void go()
    {
        // Kafka consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ResponseDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());

        KafkaConsumer<String, Response> consumer = new KafkaConsumer<>(consumerProps);

        String topic = RESPONSE_TOPIC;
        TopicPartition topicPartition = new TopicPartition(topic, 0);

        consumer.assign(singletonList(topicPartition));
        consumer.seek(topicPartition, 0L);

        System.out.println("Assigned to (Polling) " + topic);

        while(true)
        {
            ConsumerRecords<String, Response> records = consumer.poll(1000);
            for (ConsumerRecord<String, Response> consumerRecord : records)
            {
                processResponse(consumerRecord.value());
            }
        }
    }

    private final Map<UUID, Response> uuidToResponse = new HashMap<>();
    private void processResponse(Response queryResponse)
    {
        Response firstResponse = uuidToResponse.get(queryResponse.uuid);
        if(firstResponse == null)
        {
            System.out.print("+");
            uuidToResponse.put(queryResponse.uuid, queryResponse);
        }
        else
        {
            if (queryResponse.type.equals(firstResponse.type))
            {
                System.out.print("✔");
            }
            else
            {
                System.out.printf("%nError: Response not matching:%n" +
                                "Actual: " + firstResponse + "%n" +
                                "Expected: " + queryResponse + "%n");
            }
        }
    }
}
