package education;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UuidTest
{
    @Test
    public void testEquality()
    {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        UUID converted = UUID.fromString(uuidString);
        assertThat(converted).isEqualTo(uuid);
    }
}
