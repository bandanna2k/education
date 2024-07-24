package casestudy.bank.serde.requests;

import com.fasterxml.jackson.databind.ObjectReader;
import education.jackson.requests.Request;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static casestudy.bank.serde.requests.RequestSerializer.OBJECT_MAPPER;

public class RequestDeserializer implements Deserializer<Request>
{
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(Request.class);

    RequestDeserializer()
    {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey)
    {
    }

    @Override
    public Request deserialize(String topic, byte[] data)
    {
        if (data == null)
        {
            System.out.println("Null received at deserializing");
            return null;
        }
        try
        {
            return MESSAGE_READER.readValue(new String(data, StandardCharsets.UTF_8), Request.class);
        }
        catch (Exception e)
        {
            throw new SerializationException("Error when deserializing byte[] to Request");
        }
    }

    @Override
    public void close()
    {
    }
}
