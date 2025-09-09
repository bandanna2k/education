package education.syntax.parsing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ParseBooleanTest
{
    @Test
    public void shouldParseNull()
    {
        assertThat(Boolean.parseBoolean(null)).isFalse();
    }
}
