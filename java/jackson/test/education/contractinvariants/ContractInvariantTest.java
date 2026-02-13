package education.contractinvariants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.core.Configuration;
import org.assertj.core.api.Assertions;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;


public class ContractInvariantTest {

    private final String contract = """
                {
                    "id": "${json-unit.any-number}",
                    "name": "${json-unit.any-string}",
                    "email": "${json-unit.any-string}",
                    "phone": "${json-unit.ignore-element}"
                }
                """;
    private final Configuration config = Configuration
            .empty()
            .withMatcher("nullOrInteger", new NullOrIntegerMatcher());


    @Test
    void shouldPassWithNoPhoneNumber() throws JsonProcessingException {
        final String message = """
                {
                  "id": 123,
                  "name": "John Doe",
                  "email": "john@example.com"
                }
                """;
        assertContract(message);
    }

    private void assertContract(String message) throws JsonProcessingException {
        validatePhone(message);
        assertJsonEquals(contract, message, config);
    }

    @Test
    void shouldFailWithBadPhoneNumber() throws JsonProcessingException {
        final String message = """
                {
                  "id": 123,
                  "name": "John Doe",
                  "email": "john@example.com",
                  "phone": "555-123-4567"
                }
                """;
        Assertions.assertThatExceptionOfType(AssertionFailedError.class)
                .isThrownBy(() -> assertContract(message));
    }

    @Test
    void shouldPassWithIntegerPhoneNumber() throws JsonProcessingException {
        final String message = """
                {
                  "id": 123,
                  "name": "John Doe",
                  "email": "john@example.com",
                  "phone": "0"
                }
                """;
        assertContract(message);
    }

    @Test
    void shouldFailWithNoId() {
        final String message = """
                {
                  "name": "John Doe",
                  "email": "john@example.com",
                  "phone": "0"
                }
                """;
        Assertions.assertThatExceptionOfType(AssertionFailedError.class)
                        .isThrownBy(() -> assertContract(message));
    }

    public static class NullOrIntegerMatcher extends BaseMatcher<Object> {
        @Override
        public boolean matches(Object item) {
            return switch (item) {
                case null -> true;
                case Integer ignored -> true;
                case Long ignored -> true;
                default -> false;
            };
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("integer or null");
        }
    }

    private static void validatePhone(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(message);

        if (node.has("phone")) {
            JsonNode phone = node.get("phone");
            try {
                Long.parseLong(phone.asText());
            } catch (NumberFormatException e) {
                throw new AssertionFailedError(
                        "phone must be integer or null, got: " + phone);
            }
        }
    }
}
