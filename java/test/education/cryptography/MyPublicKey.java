package education.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class MyPublicKey
{
    private final Cipher encryptCipher;
    private final PublicKey key;

    private final byte[] keyBytes;
    private final Signature signature;

    public MyPublicKey(byte[] publicKey)
    {
        try
        {
            key = createPublicKey(publicKey);

            signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(key);

            keyBytes = key.getEncoded();

            encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static MyPublicKey createKey(final byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        return new MyPublicKey(createPublicKey(keyBytes).getEncoded());
    }
    private static PublicKey createPublicKey(final byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }

    public byte[] encrypt(byte[] input)
    {
        try
        {
            return encryptCipher.doFinal(input);
        }
        catch (IllegalBlockSizeException | BadPaddingException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "MyPublicKey{" +
                "encryptCipher=" + encryptCipher +
                ", key=" + key +
                ", keyBytes=" + Base64.getEncoder().encodeToString(keyBytes) +
                '}';
    }

    public boolean isSigned(byte[] message, byte[] signed)
    {
        try
        {
            signature.update(message);
            return signature.verify(signed);
        }
        catch (SignatureException e)
        {
            throw new RuntimeException(e);
        }
    }
}
