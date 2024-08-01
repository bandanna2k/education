package education.cryptography;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Base64;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

/**
 * Encrypt data with symmetrical cipher
 *
 * Attach a header with the symmetrical key encrypted with each individuals public key.
 * Now the data can be obtained by all parties.
 */
public class MultiPartyEncryptionTest
{
    // TODO https://medium.com/@daser/a-lazy-mans-introduction-to-multi-party-encryption-and-decryption-59f62b8616d8

    private String text = "The quick brown fox jumps over the lazy dog.";
    private SymmetricalEncryptor symmetricalEncryptor;
    private KeyPair kp1;
    private KeyPair kp2;
    private KeyPair kp3;
    private KeyPair kp4;

    @Before
    public void setUp() throws Exception
    {
        System.out.println("Text:" + text);

        symmetricalEncryptor = new SymmetricalEncryptor();
        kp1 = new KeyPair();
        kp2 = new KeyPair();
        kp3 = new KeyPair();
        kp4 = new KeyPair();
    }

    @Test
    @Ignore
    public void canMultiplePersonsReadEncryptedData()
    {
        byte[] encrypted;

        encrypted = encrypt(text.getBytes(), symmetricalEncryptor, kp1.getPublicKey(),kp2.getPublicKey(),kp3.getPublicKey(),kp4.getPublicKey());
        System.out.println("Encrypted:" + new String(encrypted));
        System.out.println("Encrypted:" + Base64.getEncoder().encodeToString(encrypted));

        final byte[] decrypted1 = decrypt(kp1.getPrivateKey(), encrypted);
        final byte[] decrypted2 = decrypt(kp1.getPrivateKey(), encrypted);
        final byte[] decrypted3 = decrypt(kp3.getPrivateKey(), encrypted);
        final byte[] decrypted4 = decrypt(kp4.getPrivateKey(), encrypted);
        assertThat(new String(decrypted1),is(equalTo(text)));
        assertThat(new String(decrypted2),is(equalTo(text)));
        assertThat(new String(decrypted3),is(equalTo(text)));
        assertThat(new String(decrypted4),is(equalTo(text)));

        System.out.println("Decrypted:" + new String(decrypted1));
        System.out.println("Decrypted:" + new String(decrypted2));
        System.out.println("Decrypted:" + new String(decrypted3));
        System.out.println("Decrypted:" + new String(decrypted4));
    }

    @Test
    public void canAnybodyReadTheData()
    {
        byte[] encrypted;

        encrypted = encrypt(text.getBytes(), symmetricalEncryptor,kp1.getPublicKey(),kp2.getPublicKey(),kp3.getPublicKey(),kp4.getPublicKey());
        System.out.println("Encrypted:" + new String(encrypted));

        assertThat(new String(encrypted),is(not(equalTo(text))));
    }

    public byte[] encrypt(byte[] input, SymmetricalEncryptor symmetricalEncryptor, MyPublicKey... keys)
    {
        int length = input.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = input[length - i - 1];
        }
        return result;
    }

    public byte[] encrypt(byte[] input, SymmetricalEncryptor symmetricalEncryptor)
    {
        return symmetricalEncryptor.encrypt(input);
//        int length = input.length;
//        byte[] result = new byte[length];
//        for (int i = 0; i < length; i++) {
//            result[i] = input[length - i - 1];
//        }
//        return result;
    }

    public byte[] decrypt(byte[] input, SymmetricalEncryptor symmetricalEncryptor)
    {
        return symmetricalEncryptor.decrypt(input);
    }

    public byte[] decrypt(MyPrivateKey key, byte[] encrypted)
    {
        return decrypt(encrypted, null);
    }
}
