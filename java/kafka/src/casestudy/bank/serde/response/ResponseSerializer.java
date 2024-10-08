package casestudy.bank.serde.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import education.jackson.requests.Request;
import education.jackson.response.Response;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ResponseSerializer implements Serializer<Response>
{
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Override
    public void configure(Map<String, ?> configs, boolean isKey)
    {
    }

    @Override
    public byte[] serialize(String topic, Response data)
    {
        if (data == null)
        {
            System.out.println("Null received at serializing");
            return null;
        }
        try
        {
//            System.out.println("Serializing..." + OBJECT_MAPPER.writeValueAsString(data));
            return OBJECT_MAPPER.writeValueAsBytes(data);
        }
        catch (Exception e)
        {
            throw new SerializationException("Error when serializing Request to byte[]");
        }
    }

    @Override
    public void close()
    {
    }
}