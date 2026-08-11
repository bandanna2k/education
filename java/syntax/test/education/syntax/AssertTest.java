package education.syntax;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AssertTest
{
    @BeforeAll
    static void beforeAll() {
        System.setProperty("CREATE_NEW_OBJECT", "true");
    }

    @Test
    public void shouldThrowOnAssertNotMet()
    {
        assertThatThrownBy(() -> {
            assert false : "Made up assertion not met.";
                })
                .isInstanceOf(AssertionError.class)
                .withFailMessage("Made up assertion not met.");
    }

    @Test
    void multipleAssertsUsingJuniper() {
        final Object o = null;
        assertAll(
                () -> assertThat(o).describedAs("Is not null").isNotNull(),
                () -> assertThat(o).describedAs("Is instance of").isInstanceOf(Object.class)
        );
    }

    @Test
    void multipleAsserts() {
        assertThat("")
                .satisfies(
                a -> assertThat(a).as("contains").contains("quick"),
                a -> assertThat(a).as("contains").contains("brown"),
                a -> assertThat(a).as("contains").contains("fox"));
    }
}
