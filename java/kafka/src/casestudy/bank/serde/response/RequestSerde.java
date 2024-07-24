package casestudy.bank.serde.response;

import education.jackson.requests.Request;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class RequestSerde implements Serde<Request>
{
    private Serializer serializer = new ResponseSerializer();
    private Deserializer deserializer = new ResponseDeserializer();

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
    public Serializer<Request> serializer()
    {
        return serializer;
    }

    @Override
    public Deserializer<Request> deserializer()
    {
        return deserializer;
    }
}