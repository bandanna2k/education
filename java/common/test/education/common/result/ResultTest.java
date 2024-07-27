package education.common.result;

import org.junit.Test;

import static education.common.result.Result.failure;
import static education.common.result.Result.success;
import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest
{
    private final Result<Integer, Void> success1 = success(1);
    private final Result<Integer, Void> success2 = success(2);
    private final Result<Void, Long> failure3 = failure(3L);
    private final Result<Void, Long> failure4 = failure(4L);

    @Test
    public void testMap()
    {
        Result<Integer, Void> map = success1.map(success2);
        assertThat(map).isEqualTo(2);

        Result<Integer, Void> map1 = success1.map(success2);
        assertThat(map1).isEqualTo(2);
    }
}