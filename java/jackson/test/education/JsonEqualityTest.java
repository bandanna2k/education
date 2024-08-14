package education;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class JsonEqualityTest
{
    @Test
    public void areObjectsEqual() throws JsonProcessingException
    {
        String object2 = STR."""
                {
                    "name": "bob",
                    "age": 20
                }
                """;
        String object1 = STR."""
                {
                    "age": 20,
                    "name": "bob"
                }
                """;
        assertThat(object1).isNotEqualTo(object2);
        assertThat(String.CASE_INSENSITIVE_ORDER.compare(object1, object2)).isNotEqualTo(0);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode1 = mapper.readTree(object1);
        JsonNode jsonNode2 = mapper.readTree(object2);
        System.out.println(jsonNode1);
        System.out.println(jsonNode2);
        assertEquals(jsonNode1, jsonNode2);
    }
}
