package examples.withStreams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class MyStreams
{
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String TOPIC = "test-topic";

    public static void main(String[] args)
    {
        new MyStreams().go();
    }

    private void go() {
        initStreams();

    }

    private void initStreams()
    {
        Properties streamProperties = new Properties();
        streamProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        streamProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-streamer");
        streamProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> textLines = streamsBuilder.stream(TOPIC);
        textLines.foreach((s, s2) ->
        {
            String x = s + " " + s2;
            System.out.println(x);
        });

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
            streams.start();

            System.out.printf("Sleeping%n");
            Thread.sleep(1_000_000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}