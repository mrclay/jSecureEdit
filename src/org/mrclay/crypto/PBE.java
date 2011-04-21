package org.mrclay.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Password-based encryption (PKCS #5). To change the salt or number of
 * iterations, set the static properties before creating an instance.
 *
 * To avoid a runtime error, the user must download "Cryptography Extension (JCE) Unlimited
 * Strength Jurisdiction Policy Files 6" and place the two java files in <JRE>/lib/security
 * (over top of the default ones). And similarly for debugging: <JDK>/jre/lib/security
 * @link http://www.oracle.com/technetwork/java/javase/downloads/index.html
 *
 * @see http://www.ietf.org/rfc/rfc2898.txt
 * @see http://stackoverflow.com/questions/992019/java-256bit-aes-encryption/992413#992413
 * @author Steve Clay http://www.mrclay.org/
 */
public class PBE {

    /**
     * @param password
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public PBE(char[] password) throws InvalidKeySpecException,
                                       NoSuchAlgorithmException,
                                       NoSuchPaddingException
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, 1024, 256);
        secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    /**
     * encrypt a byte array
     * @param cleartext
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws InvalidParameterSpecException
     */
    public PBEStorage encrypt(byte[] cleartext) throws IllegalBlockSizeException,
                                                       BadPaddingException,
                                                       InvalidKeyException,
                                                       InvalidParameterSpecException
    {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return new PBEStorage(
            cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV()
            ,cipher.doFinal(cleartext)
        );
    }

    /**
     * decrypt a PBEStorage object (IV and ciphertext)
     * @param storage
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    public byte[] decrypt(PBEStorage storage) throws BadPaddingException,
                                                     IllegalBlockSizeException,
                                                     InvalidAlgorithmParameterException,
                                                     InvalidKeyException
    {
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(storage.getIv()));
        return cipher.doFinal(storage.getCiphertext());
    }
    
    /**
     * bytes used to salt the key (set before making an instance)
     */
    public static byte[] salt = {
        (byte)0xc8, (byte)0x73, (byte)0x41, (byte)0x8c,
        (byte)0x7e, (byte)0xd8, (byte)0xee, (byte)0x89
    };

    /**
     * number of times the password & salt are hashed during key creation (set before making an instance)
     */
    public static int numIterations = 1024;

    private SecretKey secretKey = null;
    private Cipher cipher = null;
}
