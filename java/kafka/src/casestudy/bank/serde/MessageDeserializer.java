package casestudy.bank.serde;

import com.fasterxml.jackson.databind.ObjectReader;
import education.jackson.Message;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static casestudy.bank.serde.MessageSerializer.OBJECT_MAPPER;

public class MessageDeserializer implements Deserializer<Message>
{
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(Message.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey)
    {
    }

    @Override
    public Message deserialize(String topic, byte[] data)
    {
        if (data == null)
        {
            System.out.println("Null received at deserializing");
            return null;
        }
        try
        {
            return MESSAGE_READER.readValue(new String(data, StandardCharsets.UTF_8), Message.class);
        }
        catch (Exception e)
        {
            throw new SerializationException("Error when deserializing byte[] to Message");
        }
    }

    @Override
    public void close()
    {
    }
}
