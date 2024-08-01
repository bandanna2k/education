package education.syntax;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AssertTest
{
    @Test
    public void shouldThrowOnAssertNotMet()
    {
        assertThatThrownBy(() -> {
            assert false : "Made up assertion not met.";
                })
                .isInstanceOf(AssertionError.class)
                .withFailMessage("Made up assertion not met.");
    }
}
