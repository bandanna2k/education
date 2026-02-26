package education.cryptography;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SymmetricalEncryptor {
    private final byte[] key;
    private final Cipher encrypter;
    private final Cipher decrypter;

    public SymmetricalEncryptor() {
        this("password");
    }

    public SymmetricalEncryptor(String password) {
        try {
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            this.key = Arrays.copyOf(digestOfPassword, 24);

            final SecretKey secretKey = new SecretKeySpec(this.key, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            encrypter = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            encrypter.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            decrypter = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decrypter.init(Cipher.DECRYPT_MODE, secretKey, iv);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(byte[] input) {
        try {
            return encrypter.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] input) {
        try {
            return decrypter.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
