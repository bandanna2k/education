package dnt.websockets.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestsTest
{
    private final ObjectMapper mapper;
    private final ObjectReader messageReader;

    public RequestsTest()
    {
        mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.registerSubtypes(new NamedType(GetPropertyRequest.class, GetPropertyRequest.class.getSimpleName()));
        messageReader = mapper.readerFor(AbstractRequest.class);
    }

    @Test
    public void testSerde() throws JsonProcessingException
    {
        GetPropertyRequest original = new GetPropertyRequest().attachCorrelationId(1);
        final String json = mapper.writeValueAsString(original);

        AbstractRequest decoded = messageReader.readValue(json);
        assertThat(decoded).isInstanceOf(GetPropertyRequest.class);
    }
}
