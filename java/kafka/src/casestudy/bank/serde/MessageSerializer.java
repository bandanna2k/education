package casestudy.bank.serde;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import education.jackson.Message;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MessageSerializer implements Serializer<Message>
{
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Override
    public void configure(Map<String, ?> configs, boolean isKey)
    {
    }

    @Override
    public byte[] serialize(String topic, Message data)
    {
        if (data == null)
        {
            System.out.println("Null received at serializing");
            return null;
        }
        try
        {
            System.out.println("Serializing..." + OBJECT_MAPPER.writeValueAsString(data));
            return OBJECT_MAPPER.writeValueAsBytes(data);
        }
        catch (Exception e)
        {
            throw new SerializationException("Error when serializing Message to byte[]");
        }
    }

    @Override
    public void close()
    {
    }
}