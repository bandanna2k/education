package education.vertx.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import education.vertx.json.subtypes.unannotated.response.Balance;
import education.vertx.json.subtypes.unannotated.response.Response;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestJson
{
    @Test
    public void testRegisterSubTypes() throws JsonProcessingException
    {
        String json;
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerSubtypes(new NamedType(Balance.class));

            json = mapper.writeValueAsString(new Balance(UUID.randomUUID(), 1, "10.0"));
        }
        System.out.println(json);
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerSubtypes(new NamedType(Balance.class));

            Response response = mapper.readValue(json, Response.class);
            assertThat(response).isInstanceOf(Balance.class);
        }
    }
}
