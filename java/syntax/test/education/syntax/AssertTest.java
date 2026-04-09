package education.syntax;

import org.assertj.core.api.Assertions;
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
        final Object o = Boolean.parseBoolean(System.getProperty("CREATE_NEW_OBJECT", "false")) ? new Object() : null;
        assertAll(
                () -> assertThat(o).describedAs("Is not null").isNotNull(),
                () -> assertThat(o).describedAs("Is instance of").isInstanceOf(Object.class)
        );
    }
}
