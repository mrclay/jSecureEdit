/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mrclay.SecureEdit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.mrclay.compression.Deflate;
import org.mrclay.crypto.PBE;
import org.mrclay.crypto.PBEStorage;

public class Encryption {
    public static final String ENCRYPTED_TOKEN = "https://github.com/mrclay/jSecureEdit *** ";

    private PBE pbe = null;

    public void setPassword(char[] password) throws InvalidKeySpecException,
                                                    NoSuchAlgorithmException,
                                                    NoSuchPaddingException
    {
        if (password.length == 0) {
            pbe = null;
        } else {
            pbe = new PBE(password);
        }
    }

    public boolean isReady()
    {
        return (pbe != null);
    }

    public boolean looksDecryptable(String str) {
        return str.startsWith(ENCRYPTED_TOKEN);
    }

    public String encrypt(String cleartext) throws BadPaddingException,
                                                   IllegalBlockSizeException,
                                                   InvalidKeyException,
                                                   InvalidParameterSpecException,
                                                   UnsupportedEncodingException,
                                                   IOException
    {
        PBEStorage storage = pbe.encrypt(Deflate.deflate(cleartext.getBytes("UTF-8")));
        return ENCRYPTED_TOKEN + storage;
    }

    public String decrypt(String fileContent)  throws BadPaddingException,
                                                      IllegalBlockSizeException,
                                                      InvalidAlgorithmParameterException,
                                                      InvalidKeyException,
                                                      UnsupportedEncodingException
    {
        PBEStorage storage = new PBEStorage(fileContent.substring(ENCRYPTED_TOKEN.length()));
        return new String(Deflate.inflate(pbe.decrypt(storage)), "UTF-8");
    }
}
