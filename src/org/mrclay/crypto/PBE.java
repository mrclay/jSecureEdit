package org.mrclay.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.apache.commons.codec.binary.Base64;

/**
 * Possibly weak password-based encryption (PKCS #5). To change the salt or number of
 * iterations, set the static properties before creating an instance.
 *
 * @todo replace this with http://stackoverflow.com/questions/992019/java-256bit-aes-encryption/992413#992413
 * @todo IV needs to be stored Base64-encoded next to the ciphertext
 *
 * @see http://www.ietf.org/rfc/rfc2898.txt
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
    public PBE(char[] password) throws InvalidAlgorithmParameterException,
                                       InvalidKeyException,
                                       InvalidKeySpecException,
                                       NoSuchAlgorithmException,
                                       NoSuchPaddingException,
                                       UnsupportedEncodingException
    {
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, numIterations);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);

        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        
        // Create ciphers
        encryptCipher = Cipher.getInstance("PBEWithMD5AndDES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        decryptCipher = Cipher.getInstance("PBEWithMD5AndDES");
        decryptCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
    }

    /**
     * Encrypt a string
     * @param cleartext
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] encrypt(String cleartext) throws IllegalBlockSizeException,
                                                   BadPaddingException,
                                                   UnsupportedEncodingException
    {
        return encryptCipher.doFinal(cleartext.getBytes("UTF-8"));
    }

    public String encryptToBase64(String cleartext) throws BadPaddingException,
                                                           IllegalBlockSizeException,
                                                           UnsupportedEncodingException
    {
        return Base64.encodeBase64String(this.encrypt(cleartext));
    }

    /**
     * Decrypt a byte array
     * @param ciphertext
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public String decrypt(byte[] ciphertext) throws BadPaddingException,
                                                    IllegalBlockSizeException
    {
        return new String(decryptCipher.doFinal(ciphertext));
    }
    
    public String decryptFromBase64(String base64ciphertext) throws BadPaddingException,
                                                                    IllegalBlockSizeException
    {
        return this.decrypt(Base64.decodeBase64(base64ciphertext));    
    }

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public Cipher getDecryptCipher() {
        return decryptCipher;
    }

    public Cipher getEncryptCipher() {
        return encryptCipher;
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
    public static int numIterations = 1000;
}
