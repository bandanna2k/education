package education.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class MyPrivateKey
{
    private final Cipher decryptCipher;
    private final PrivateKey key;
    private final byte[] keyBytes;
    private final Signature signature;

    public MyPrivateKey(byte[] keyBytes)
    {
        try
        {
            key = createPublicKey(keyBytes);

            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(key);

            this.keyBytes = key.getEncoded();

            decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static MyPrivateKey createKey(final byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        return new MyPrivateKey(createPublicKey(keyBytes).getEncoded());
    }
    private static PrivateKey createPublicKey(final byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec keyspec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(keyspec);
    }


    @Override
    public String toString() {
        return "MyPrivateKey{" +
                "decryptCipher=" + decryptCipher +
                ", key=" + key +
                ", keyBytes=" + Base64.getEncoder().encodeToString(keyBytes) +
                '}';
    }

    public byte[] decrypt(byte[] encryptedBytes)
    {
        try
        {
            return decryptCipher.doFinal(encryptedBytes);
        }
        catch (IllegalBlockSizeException | BadPaddingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public byte[] sign(byte[] bytes)
    {
        try
        {
            signature.update(bytes);
            return signature.sign();
        }
        catch (SignatureException e)
        {
            throw new RuntimeException(e);
        }
    }
}
