package education.cryptography;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class SymmetricalEncryptionTest
{
    @Test
    public void canEncryptAndDecrypt()
    {
        String input = "The quick brown fox jumps over the lazy dog";
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        System.out.println("Input:" + input);
        System.out.println("Input:" + Base64.getEncoder().encodeToString(inputBytes));

        SymmetricalEncryptor key = new SymmetricalEncryptor();
        byte[] encrypted = key.encrypt(inputBytes);
        System.out.println("Encrypted:" + Base64.getEncoder().encodeToString(encrypted));

        byte[] decryptedBytes = key.decrypt(encrypted);
        final String decrypted = new String(decryptedBytes, StandardCharsets.UTF_8);

        System.out.println("Decrypted:" + Base64.getEncoder().encodeToString(decryptedBytes));
        System.out.println("Decrypted:" + decrypted);

        assertThat(input).contains(decrypted);
    }
}
