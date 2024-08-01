package education.common.strings;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CamelCaseTest
{
    @Test
    public void testToCamelCaseWithSpaces()
    {
        Assertions.assertThat(CamelCase.toCamelCase("blah")).isEqualTo("blah");

        Assertions.assertThat(CamelCase.toCamelCase("Camel case")).isEqualTo("camelCase");
        Assertions.assertThat(CamelCase.toCamelCase("camel case")).isEqualTo("camelCase");
        Assertions.assertThat(CamelCase.toCamelCase("CAMEL CASE")).isEqualTo("camelCase");
    }

    @Test
    public void testToCamelCaseWithDashes()
    {
        Assertions.assertThat(CamelCase.toCamelCase("Camel-Case")).isEqualTo("camel-case");
        Assertions.assertThat(CamelCase.toCamelCase("CAMEL-CASE")).isEqualTo("camel-case");
        Assertions.assertThat(CamelCase.toCamelCase("camel-case")).isEqualTo("camel-case");
    }

    @Test
    public void testToCamelCaseWithUnderScore()
    {
        Assertions.assertThat(CamelCase.toCamelCase("camel_case")).isEqualTo("camel_case");
        Assertions.assertThat(CamelCase.toCamelCase("Camel_Case")).isEqualTo("camel_case");
        Assertions.assertThat(CamelCase.toCamelCase("CAMEL_CASE")).isEqualTo("camel_case");
    }
}
