package education.vertx.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import education.vertx.json.registersubtypes.response.Balance;
import education.vertx.json.registersubtypes.response.Error;
import education.vertx.json.registersubtypes.response.Response;
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

    @Test
    public void testDecodingStrings() throws JsonProcessingException
    {
        ObjectMapper mapper = getExtendedObjectMapper();
        {
            String balanceJson = STR."""
                {
                    "type": "Balance",
                    "uuid": "60964922-fb96-45a0-af98-c26d87b78044",
                    "balance":10.0,
                    "accountId":1
                }
                """;
            Response response = mapper.readValue(balanceJson, Response.class);
            assertThat(response).isInstanceOf(Balance.class);
        }
        {
            String errorJson = STR."""
                {
                    "type": "Error",
                    "uuid": "60964922-fb96-45a0-af98-c26d87b78044",
                    "error": "Not found."
                }
                """;
            Response response = mapper.readValue(errorJson, Response.class);
            assertThat(response).isInstanceOf(Error.class);
        }
    }

    private static ObjectMapper getExtendedObjectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSubtypes(new NamedType(Balance.class));
        mapper.registerSubtypes(new NamedType(Error.class));
        return mapper;
    }
}
