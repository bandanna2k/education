package education.syntax.parsing;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

public class ParseBooleanTest
{
    @Test
    public void shouldParseNull()
    {
        assertThat(Boolean.parseBoolean(null)).isFalse();
    }
}
