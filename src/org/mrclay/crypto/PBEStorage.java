
package org.mrclay.crypto;

import MiG.Base64;

/**
 *
 * @author mrclay
 */
public class PBEStorage {
    private byte[] iv;
    private byte[] ciphertext;
    public static String separator = "|";

    public PBEStorage(byte[] iv, byte[] ciphertext) {
        this.iv = iv;
        this.ciphertext = ciphertext;
    }

    public PBEStorage(String base64) {
        this(base64, separator);
    }

    public PBEStorage(String base64, String separator) {
        int loc = base64.indexOf(separator);
        iv = Base64.decode(base64.substring(0, loc));
        ciphertext = Base64.decode(base64.substring(loc + separator.length()));
    }

    public byte[] getCiphertext() {
        return ciphertext;
    }

    public byte[] getIv() {
        return iv;
    }

    @Override
    public String toString() {
        return Base64.encodeToString(iv, true) + separator + Base64.encodeToString(ciphertext, true);
    }
}
