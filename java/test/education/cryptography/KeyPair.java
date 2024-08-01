package education.cryptography;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class KeyPair
{
    private Optional<MyPublicKey> publicKey;
    private Optional<MyPrivateKey> privateKey;

    public KeyPair() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            java.security.KeyPair pair = generator.generateKeyPair();

            MyPublicKey publicKey = new MyPublicKey(pair.getPublic().getEncoded());
            MyPrivateKey privateKey = new MyPrivateKey(pair.getPrivate().getEncoded());

            init(Optional.of(publicKey), Optional.of(privateKey));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void init(Optional<MyPublicKey> publicKey, Optional<MyPrivateKey> privateKey)
    {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public MyPublicKey getPublicKey() {
        return publicKey.get();
    }

    public MyPrivateKey getPrivateKey() {
        return privateKey.get();
    }
}
