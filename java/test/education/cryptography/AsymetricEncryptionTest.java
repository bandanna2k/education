package education.cryptography;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AsymetricEncryptionTest
{
    private static final String PUBLIC_KEY_STRING =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1gjYI5T/84gIcV9GCl+1fdw2O6UxbM0/VELj8cJ99rGqsFhh1W7OZU" +
                    "jNX2PoKl8ZErtp7tRZqTrk16him7OmNCk1fZaCuDfIfXJ02XKv3jOFjE8jK6yixvFy/6VSSmjkuiZ5PaBB4u5+EUuq" +
                    "IIEg/XdlvVwct1yByKo9zm+n7gHCUkCBuH66S/trfXkeKF4Vgo9TfmIddShsblVX99zsefuF9/LLCl1h7/wlZL9fMM" +
                    "XVYgYyr6pZ0VclXx278B/yKcNOBorzzPczREP/7nP4bA/Ed16pk48J9t9a/drALkk5fSnwHP0cG1egt0+frNE0srmJ" +
                    "01HNxKjUKPrgz+ol1wIDAQAB";
    private static final String PRIVATE_KEY_STRING =
            "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDWCNgjlP/ziAhxX0YKX7V93DY7pTFszT9UQuPxwn32saqw" +
                    "WGHVbs5lSM1fY+gqXxkSu2nu1FmpOuTXqGKbs6Y0KTV9loK4N8h9cnTZcq/eM4WMTyMrrKLG8XL/pVJKaOS6Jnk9" +
                    "oEHi7n4RS6oggSD9d2W9XBy3XIHIqj3Ob6fuAcJSQIG4frpL+2t9eR4oXhWCj1N+Yh11KGxuVVf33Ox5+4X38ssK" +
                    "XWHv/CVkv18wxdViBjKvqlnRVyVfHbvwH/Ipw04GivPM9zNEQ//uc/hsD8R3XqmTjwn231r92sAuSTl9KfAc/Rwb" +
                    "V6C3T5+s0TSyuYnTUc3EqNQo+uDP6iXXAgMBAAECggEAJkcJpH9qsNp0rBXeWE+ajSo7Vrpp1uvz9fVKf7uJPHu0" +
                    "q3n68SkyZ0G4XOVoqV89hDCZNtmqOU4ri/f0vqTHxg9SniWzNg/f3rN/+z17/1CWgyvJ/e8PY2Xd66NczqrosPPd" +
                    "fexxrAx75kryX4LIbBwUyWM2TYKRQVpU2Fz2muVI6hGXFmrxkcEgr6eBDjKI/dP1W+/3XZmLPKfqFWGy+r9tECxt" +
                    "lFcV1BTJfUgTeNe6BIiJZAIwBu68CsP7sv8+5qOqWEEWo6ooXQ2noIRzbOwOIFeDPvyefM/khOM6VwBxd4Fl1dQ2" +
                    "eIhlEgjP6KpYu7KN81ac3GIxRSiNyN5aEQKBgQDql1mAZRE8/Eksuhd6es9JYy2zp4I6+HrP77TDSHMz9qoXFgu2" +
                    "/GE+vPHJdHjyBubaBhLu4/JGyaKfqjftjlU7004umV5s1vewamh3cog24dWwtyXv148YZVE+Lwf74JJ3ltuJgCV+" +
                    "OWG0jCYq/P2tHBdFfZIQMa6js1OlaYfuBwKBgQDpkT0PPTawIixPlhwLww8xHMW2KgrD11XuDG8BRwueAj6Tnl39" +
                    "8dDi8oFrMZpyhEKVs1QJrEwprX3fvVmaSgHM2TawAwXeegFNM8ftCoxGT0wg6bfujGt2N4vrzJF0PIdnjRAAYwZ2" +
                    "H+31dsVVPuLjB38ZFD5EbVppQ12gIFgVsQKBgC3BzaxUilM55k/6EEBMmgf3d1+Wabl05AeQ+tIoXIgjMbQFEc3k" +
                    "Vld1ZTflJv4WSIr4KlM8VPJnu/emrPgCDi/4HETMl3VlTLS1XKEHzvfdft/UKWspwXiBsqINbGI9wHPhEfs06ps/" +
                    "OAaOW5eA9z3/v65HOuk7t5Ook8Us212bAoGAOxi6gE6Wtb4PxBtRAVDbWSvUgqDklGS5oz+WHpahQ9uBhcw+L/Ct" +
                    "T57YTA0C7RK13ja/fqsPokP5Y1D/iOBM0E3tXl+FbqMkDSqa3Ukb8PZDZth752m0DvdC+eiKrQRbpkG02MQ1cFHn" +
                    "3YGtsEH3GkZdTDUpBBs/Ev8ug0NrxiECgYB/mjd4CpGNVFuTh2AhTkP7YRpxACo1gRI49EdUhUhY9edl4PECULPy" +
                    "Hm/P6PwEaWHXs3tn1/oqiBdbOax7t3ivpw+wk+rqiODCiqMGiP41eQ5SNJHfwtiyJ3qLdIhzVYUwLmtXeGveHWZq" +
                    "//OrAoeRLRRS8Cb7LG+dByMO+zxm2w==";
    private static final String PUBLIC_KEY_STRING2 =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxE9aZCA8xtiAfkyx9K6QBydj4GE8xQ1A4KKIqfrYGqoMxo" +
                    "ZuNDDZOq6+N3QFcQHD+DRsyfX3GDa0IdfuMyOKWP68wM4MGunbtM+V/UXY7T0xIq7ylv4vkAfVELsK1KVNGVwZtmT/120OQ" +
                    "aj91ZLF+2yrsS8CBjPr2gJ98ynir7FwWzI+xd140YEIG2cqf2WTGmV83ZxsmLiCSMepuyo7PlnEYIBxhkX2aT7PdJNynSoj" +
                    "dgPRnuDc8Y2TO+SSeTMjfNk8Cpv22MH7wDl3E2KBTsi/t6MGA4KsZRAMn0O1N4hdSerHqOVuTOVyWo/OLGCKbe1P6JoyORa" +
                    "Orq9n1qyAsQIDAQAB";
    private static final String PRIVATE_KEY_STRING2 =
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDET1pkIDzG2IB+TLH0rpAHJ2PgYTzFDUDgooip+" +
                    "tgaqgzGhm40MNk6rr43dAVxAcP4NGzJ9fcYNrQh1+4zI4pY/rzAzgwa6du0z5X9RdjtPTEirvKW/i+QB9UQuwrUpU0ZXBm" +
                    "2ZP/XbQ5BqP3VksX7bKuxLwIGM+vaAn3zKeKvsXBbMj7F3XjRgQgbZyp/ZZMaZXzdnGyYuIJIx6m7Kjs+WcRggHGGRfZpP" +
                    "s90k3KdKiN2A9Ge4NzxjZM75JJ5MyN82TwKm/bYwfvAOXcTYoFOyL+3owYDgqxlEAyfQ7U3iF1J6seo5W5M5XJaj84sYIp" +
                    "t7U/omjI5Fo6ur2fWrICxAgMBAAECggEALYgcNon1nsc7ck/WnKJaXJZpx3K2NhwfVprMKOS59+FGKkumxusnj0FVdCYwV" +
                    "ugqbGi4bNVkVNTnTcw3/tt47LBU1kFUNG/WWiclqkNnj4r4WQuLz7KwFG+EnydK2UxnyACBJDljz7MHMfqAuWccDXdEkUm" +
                    "OZiLnyr52OQ6Xedk9bSe8MlLpHMzI5BRiheX8j33fw3kbvjwK0cYSt53GaGMmuaDhnD4tDxOlFCjsNSq3nY1/pDDWk4wqO" +
                    "xXMY6aY5wEO8SSpOtRu+EMUiH7iCjFjw3eZNbpijPvE64dG5Sm7BXowY/9+7M8FLtt4WWpaRkw3rrSNAzaBzVfc6u3zMwK" +
                    "BgQDj4YKo3fDzaUPCdhoXAVPjRTu3LsDdZfqXwOFaGt4nEgjkgwq1qcf2rG4RV+auFCIeg0yIrCqERMom/Qp0XWLFCID2t" +
                    "J/EmsW5WBL1hYFkvI1cXKKgKyRyimI1XhCmug89i3z0OSEBSWvchB6SlrxZpR0cmn2X4lCZ9VGEWr8NnwKBgQDciI3jW/g" +
                    "EPMO9fhKMzkkFPI33ekfOBzw+Y1ak82BucOtCUxIyPQxjoyuzg3eeNfhVyHM957+wXujDqEm+bJpkEcWZQym4mquMtV460" +
                    "6wRjBG0vHuXPo+eGOuIdwNnm7OckeeieZzzVi9J9/ItNlK92usHgo1pj7bXJPkauC4vrwKBgEuW40t2i1WCplpb6J2W8Ce" +
                    "kMIG7/XezOyOZdGb6IJSfM9nw6d2GmvDlE4GnQbqerQ6oCPwQ9+12EARMzWn6DChFgNnU4mZJ3Kp1X5yXk2tB7DYl+D/e7" +
                    "7Ea3CbNtAJKF4IXwfQU+zC9FQ1JugZv/7xQu85gJKb8CDZ5nHelzlgLAoGAFX/GfVS48AjQeEezHUD7A2Ss3mRwXZh8+gQ" +
                    "LLVeAniOp+7RHgNhMveXpW73ESosuY4aAHZfbKyGFhZWBYuwJfRnyboeKqLGDadtAjXGXQ7qo+zqovczeiWYaiBo6Hb6jG" +
                    "u9qan96mopbX9WpKVkzZcjSws+qY5EcHQtAsack8YcCgYEAqCF9vb4UMEhOowpZXQI3QxYMqTgIP9hyFrxWBnXWi3nUkAa" +
                    "UbbIlDxDNGY6YYRfM68fWQtr8RufbwMd4JVxoSmBc38r+EMLTyymT+RskdtI0B8ufqDK4nrD0RdpAqtaMEzmCP+nR1OYpg" +
                    "7+/p6FryFn4PlZ/aN46J0fQTJNV8VA=";

    @Test
    public void canBuildPublicAndPrivateKeys() throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        final MyPublicKey publicKey = MyPublicKey.createKey(Base64.getDecoder().decode(PUBLIC_KEY_STRING));
        final MyPrivateKey privateKey = MyPrivateKey.createKey(Base64.getDecoder().decode(PRIVATE_KEY_STRING));

        checkPublicPrivateKeys(publicKey, privateKey);
    }

    @Test
    public void canBuildPublicAndPrivateKeys2() throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        final MyPublicKey publicKey = MyPublicKey.createKey(Base64.getDecoder().decode(PUBLIC_KEY_STRING2));
        final MyPrivateKey privateKey = MyPrivateKey.createKey(Base64.getDecoder().decode(PRIVATE_KEY_STRING2));

        checkPublicPrivateKeys(publicKey, privateKey);
    }

    private void checkPublicPrivateKeys(final MyPublicKey publicKey, final MyPrivateKey privateKey)
    {
        final byte[] input = "The quick brown fox jumps over the lazy dog.".getBytes(StandardCharsets.UTF_8);

        final byte[] encrypted = publicKey.encrypt(input);

        final byte[] decrypted = privateKey.decrypt(encrypted);

        assertThat(input).containsExactly(decrypted);
    }

    @Test
    public void canEncryptAndDecrypt()
    {
        KeyPair keyPair = new KeyPair();
        MyPublicKey publicKey = keyPair.getPublicKey();
        MyPrivateKey privateKey = keyPair.getPrivateKey();

        System.out.println(publicKey);
        System.out.println(privateKey);

        String input = "The quick brown fox jumps over the lazy dog";
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        System.out.println("Input:" + input);
        System.out.println("Input:" + Base64.getEncoder().encodeToString(inputBytes));

        byte[] encrypted = publicKey.encrypt(inputBytes);
        System.out.println("Encrypted:" + Base64.getEncoder().encodeToString(encrypted));

        byte[] decryptedBytes = privateKey.decrypt(encrypted);
        final String decrypted = new String(decryptedBytes, StandardCharsets.UTF_8);

        System.out.println("Decrypted:" + Base64.getEncoder().encodeToString(decryptedBytes));
        System.out.println("Decrypted:" + decrypted);

        assertThat(input).contains(decrypted);
    }

    @Test
    public void canSignAndVerify() throws Exception
    {
        KeyPair keyPair = new KeyPair();
        MyPublicKey publicKey = keyPair.getPublicKey();
        MyPrivateKey privateKey = keyPair.getPrivateKey();

        String input = "The quick brown fox jumps over the lazy dog";
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        System.out.println("Input:" + input);
        System.out.println("Input:" + Base64.getEncoder().encodeToString(inputBytes));

        final byte[] signed = privateKey.sign(inputBytes);

        System.out.println("Signed:" + Base64.getEncoder().encodeToString(signed));

        assertTrue(publicKey.isSigned(inputBytes, signed));
    }

    @Test
    public void canSignAndNotVerify() throws Exception
    {
        MyPrivateKey privateKey = MyPrivateKey.createKey(Base64.getDecoder().decode(PRIVATE_KEY_STRING));
        MyPublicKey publicKey = MyPublicKey.createKey(Base64.getDecoder().decode(PUBLIC_KEY_STRING2));

        String input = "The quick brown fox jumps over the lazy dog";
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        System.out.println("Input:" + input);
        System.out.println("Input:" + Base64.getEncoder().encodeToString(inputBytes));

        final byte[] signed = privateKey.sign(inputBytes);

        System.out.println("Signed:" + Base64.getEncoder().encodeToString(signed));

        assertFalse(publicKey.isSigned(inputBytes, signed));
    }
}
