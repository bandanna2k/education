package education.contractinvariants;

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
    void shouldPassWithNoPhoneNumber() {
        final String message = """
                {
                  "id": 123,
                  "name": "John Doe",
                  "email": "john@example.com"
                }
                """;
        assertJsonEquals(contract, message, config);
    }

    @Test
    void shouldFailWithBadPhoneNumber() {
        final String message = """
                {
                  "id": 123,
                  "name": "John Doe",
                  "email": "john@example.com",
                  "phone": "555-123-4567"
                }
                """;
        assertJsonEquals(contract, message, config);
    }

    @Test
    void shouldPassWithIntegerPhoneNumber() {
        final String message = """
                {
                  "id": 123,
                  "name": "John Doe",
                  "email": "john@example.com",
                  "phone": "0"
                }
                """;
        assertJsonEquals(contract, message, config);
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
                        .isThrownBy(() -> assertJsonEquals(contract, message, config));
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
}
