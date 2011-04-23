
package org.mrclay.SecureEdit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileIO {
    public static void writeFile(File file, String content) throws IOException
    {
        org.mrclay.io.File.writeBytes(file, content.getBytes("UTF-8"));
    }

    public static String readFile(File file) throws FileNotFoundException, IOException
    {
        byte[] byteContents = org.mrclay.io.File.readBytes(file);
        // look for our token
        String str = new String(byteContents, "UTF-8");
        if (str.contains(Encryption.DELIMETER)) {
            return str;
        } else if (str.startsWith("\uFEFF")) { // UTF-8 BOM
            // keep UTF-8, but strip BOM
            return str.substring(1);
        } else {
            // some other encoding, let Java figure it out
            // @todo use 3rd-party encoding detection
            return new String(byteContents);
        }
    }
}
