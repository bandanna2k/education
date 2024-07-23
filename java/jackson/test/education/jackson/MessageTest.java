package education.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class MessageTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(Message.class);

    @Test
    public void testSerde() throws JsonProcessingException
    {
        TestVisitor tester = new TestVisitor();
        {
            final String json;
            {
                Deposit deposit = new Deposit();
                deposit.amount = BigDecimal.valueOf(90.02);
                json = OBJECT_MAPPER.writeValueAsString(deposit);

                Message message = MESSAGE_READER.readValue(json);
                message.visit(tester);
            }
        }
        {
            final String json;
            {
                Withdrawal withdrawal = new Withdrawal();
                withdrawal.amount = BigDecimal.valueOf(66.43);
                json = OBJECT_MAPPER.writeValueAsString(withdrawal);

                Message message = MESSAGE_READER.readValue(json);
                message.visit(tester);
            }
        }
    }

    @Test
    public void testDeserialisationVisitor() throws JsonProcessingException
    {
        TestVisitor tester = new TestVisitor();
        {
            String json = STR."""
                    {
                        "type": "deposit",
                        "amount" : "50.25"
                    }
                    """;
            Message message = MESSAGE_READER.readValue(json);
            message.visit(tester);
        }
        {
            String json = STR."""
                    {
                        "type": "withdrawal",
                        "amount" : "44.77"
                    }
                    """;
            Message message = MESSAGE_READER.readValue(json);
            message.visit(tester);
        }
    }

    @Test
    public void testDeserialisationCasting() throws JsonProcessingException
    {
        String json = STR."""
                {
                    "type": "deposit",
                    "amount" : "50.25"
                }
                """;
        Message message = OBJECT_MAPPER.readerFor(Message.class).readValue(json);
        assertThat(message).isInstanceOf(Deposit.class);

        assertDeposit((Deposit)message, new BigDecimal("50.25"));


        TestVisitor tester = new TestVisitor();
        message.visit(tester);

//
//        assertMessage(OBJECT_MAPPER.readerFor(Message.class).readValue(json), new BigDecimal("50.25"));
    }
    private void assertDeposit(Deposit deposit, BigDecimal amount)
    {
        assertThat(deposit.amount).isEqualTo(amount);
    }
}
