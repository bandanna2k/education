package casestudy.bank.serde.response;

import com.fasterxml.jackson.databind.ObjectReader;
import education.jackson.response.Response;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static casestudy.bank.serde.response.ResponseSerializer.OBJECT_MAPPER;

public class ResponseDeserializer implements Deserializer<Response>
{
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(Response.class);

    ResponseDeserializer()
    {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey)
    {
    }

    @Override
    public Response deserialize(String topic, byte[] data)
    {
        if (data == null)
        {
            System.out.println("Null received at deserializing");
            return null;
        }
        try
        {
            return MESSAGE_READER.readValue(new String(data, StandardCharsets.UTF_8), Response.class);
        }
        catch (Exception e)
        {
            throw new SerializationException("Error when deserializing byte[] to Response");
        }
    }

    @Override
    public void close()
    {
    }
}
