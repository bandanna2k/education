package education.common.strings;

import java.util.Random;

public class StringGenerator
{
    private static final char[] CHARSET = "01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private final Random random;

    public StringGenerator(final Random random)
    {
        this.random = random;
    }

    public String randomString(final int size)
    {
        String result = "";
        for (int j = 0; j < size; j++)
        {
            final int index = random.nextInt(CHARSET.length);
            result += CHARSET[index];
        }
        return result;
    }
}
