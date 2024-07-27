package education.common.result;

import org.junit.Test;

import static education.common.result.Result.failure;
import static education.common.result.Result.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ResultTest
{
    private final Result<Integer, String> success1 = success(1);
    private final Result<Integer, String> success2 = success(2);
    private final Result<Void, String> failure3 = failure("3");
    private final Result<Void, String> failure4 = failure("4");

    @Test
    public void testMap()
    {
        {
            Result<Integer, String> result = success1.map(success -> success2);
            assertTrue(result.isSuccess());
            assertThat(result.success()).isEqualTo(2);
        }
        {
            Result<Void, String> result = success1.map(success -> failure3);
            assertTrue(result.hasFailed());
            assertThat(result.error()).isEqualTo("3");
        }
        {
            Result<Integer, String> result = failure4.map(success -> success1);
            assertTrue(result.hasFailed());
            assertThat(result.error()).isEqualTo("4");
        }
        {
            Result<Void, String> result = failure4.map(success -> failure3);
            assertTrue(result.hasFailed());
            assertThat(result.error()).isEqualTo("4");
        }
    }
}