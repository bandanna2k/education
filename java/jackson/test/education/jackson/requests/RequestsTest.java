package education.jackson.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestsTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(Request.class);
    private static final UUID UUID = java.util.UUID.fromString("0190e3ca-435e-7617-a5fb-9c328003a773");

    @Test
    public void testSerde() throws JsonProcessingException
    {
        TestRequestVisitor tester = new TestRequestVisitor(UUID);
        {
            final String json;
            {
                Deposit deposit = new Deposit(UUID, 1L, "90.02");
                json = OBJECT_MAPPER.writeValueAsString(deposit);

                Request request = MESSAGE_READER.readValue(json);
                request.visit(tester);
            }
        }
        {
            final String json;
            {
                Withdrawal withdrawal = new Withdrawal(UUID, 1, "66.43");
                json = OBJECT_MAPPER.writeValueAsString(withdrawal);

                Request request = MESSAGE_READER.readValue(json);
                request.visit(tester);
            }
        }
    }

    @Test
    public void testDeserialisationVisitor() throws JsonProcessingException
    {
        TestRequestVisitor tester = new TestRequestVisitor(UUID);
        {
            String json = STR."""
                    {
                        "type": "deposit",
                        "uuid" : "\{UUID}",
                        "accountId" : "1",
                        "amount" : "50.25"
                    }
                    """;
            Request request = MESSAGE_READER.readValue(json);
            request.visit(tester);
        }
        {
            String json = STR."""
                    {
                        "type": "withdrawal",
                        "uuid" : "\{UUID}",
                        "accountId" : "1",
                        "amount" : "44.77"
                    }
                    """;
            Request request = MESSAGE_READER.readValue(json);
            request.visit(tester);
        }
    }

    @Test
    public void testDeserialisationCasting() throws JsonProcessingException
    {
        String json = STR."""
                {
                    "type": "deposit",
                    "uuid" : "\{UUID}",
                    "accountId" : "1",
                    "amount" : "50.25"
                }
                """;
        Request request = OBJECT_MAPPER.readerFor(Request.class).readValue(json);
        assertThat(request).isInstanceOf(Deposit.class);

        assertDeposit((Deposit)request, new BigDecimal("50.25"));

        TestRequestVisitor tester = new TestRequestVisitor(UUID);
        request.visit(tester);
    }
    private void assertDeposit(Deposit deposit, BigDecimal amount)
    {
        assertThat(deposit.amount).isEqualTo(amount);
    }
}
