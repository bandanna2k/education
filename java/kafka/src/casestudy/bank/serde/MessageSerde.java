package casestudy.bank.serde;

import education.jackson.Message;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MessageSerde implements Serde<Message>
{
    private MessageSerializer serializer = new MessageSerializer();
    private MessageDeserializer deserializer = new MessageDeserializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey)
    {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close()
    {
        serializer.close();
        deserializer.close();
    }

    @Override
    public Serializer<Message> serializer()
    {
        return serializer;
    }

    @Override
    public Deserializer<Message> deserializer()
    {
        return deserializer;
    }
}