package education.jackson.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import education.jackson.response.Balance;
import education.jackson.response.Balances;
import education.jackson.response.Response;
import education.jackson.response.ResponseVisitor;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponsesTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(Response.class);
    private static final UUID UUID = java.util.UUID.fromString("0190e3ca-435e-7617-a5fb-9c328003a773");

    @Test
    public void testSerde() throws JsonProcessingException
    {
        TestResponseVisitor tester = new TestResponseVisitor();
        {
            final String json;
            {
                Balances balances = new Balances(UUID, List.of(
                        new Balance(null, 1L, "1000"),
                        new Balance(null, 2L, "2000"),
                        new Balance(null, 3L, "3000")
                ));
                json = OBJECT_MAPPER.writeValueAsString(balances);

                assertThat(json).doesNotContain("null");

//                Response request = MESSAGE_READER.readValue(json);
//                request.visit(tester);
            }
        }
    }
}
