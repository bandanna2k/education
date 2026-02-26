package education.cryptography;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static java.util.Base64.*;

public class Base64Test
{
    @Test
    public void testEncoding()
    {
        byte[] encoded = getEncoder().encode("varsity".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(encoded));
    }

    @Test
    public void testDecoding()
    {
        String decoded = new String(java.util.Base64.getDecoder().decode("dmFyc2l0eQ==".getBytes(StandardCharsets.UTF_8)));
        System.out.println(decoded);
    }
}
