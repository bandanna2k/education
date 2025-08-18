package education.common.result;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest
{
    @Test
    public void testMap()
    {
        {
            Result<String, String> original = Result.success("1");
            assertThat(original.success()).isInstanceOf(String.class);
            Result<Integer, Integer> mapped = original.map(Integer::parseInt, Integer::parseInt);
            assertThat(mapped.success()).isInstanceOf(Integer.class);
        }
        {
            Result<String, String> original = Result.failure("2");
            assertThat(original.error()).isInstanceOf(String.class);
            Result<Integer, Integer> mapped = original.map(Integer::parseInt, Integer::parseInt);
            assertThat(mapped.error()).isInstanceOf(Integer.class);
        }
    }

    @Test
    public void testMapSuccess()
    {
        {
            Result<Integer, Integer> original = Result.success(1);
            assertThat(original.success()).isInstanceOf(Integer.class);
            Result<String, Integer> mapped = original.map(String::valueOf);
            assertThat(mapped.success()).isInstanceOf(String.class);
        }
    }

    @Test
    public void testMapError()
    {
        {
            Result<Integer, Integer> original = Result.failure(1);
            assertThat(original.error()).isInstanceOf(Integer.class);
            Result<Integer, String> mapped = original.mapError(String::valueOf);
            assertThat(mapped.error()).isInstanceOf(String.class);
        }
    }
}