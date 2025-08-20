package dnt.websockets.communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestsTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(AbstractRequest.class);

    @Test
    public void testSerde() throws JsonProcessingException
    {
        OptionsRequest original = new OptionsRequest().attachCorrelationId(1);
        final String json = OBJECT_MAPPER.writeValueAsString(original);

        AbstractRequest decoded = MESSAGE_READER.readValue(json);
        assertThat(decoded).isInstanceOf(OptionsRequest.class);
    }
}
