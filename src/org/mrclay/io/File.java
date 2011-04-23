
package org.mrclay.io;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class File {
    public static byte[] readBytes(java.io.File file) throws FileNotFoundException, IOException
    {
        int outStreamSize = (int) Math.min(file.length(), Integer.MAX_VALUE);
        ByteArrayOutputStream bucket = new ByteArrayOutputStream(outStreamSize);
        InputStream inStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        try {
            while (true) {
                int bytesRead = inStream.read(buffer);
                if (bytesRead == -1) {
                    break;
                } else {
                    bucket.write(buffer, 0, bytesRead);
                }
            }
        }
        finally {
            inStream.close();
        }
        return bucket.toByteArray();
    }

    public static void writeBytes(java.io.File file, byte[] bytes) throws FileNotFoundException, IOException
    {
        OutputStream outStream = new FileOutputStream(file);
        try {
            outStream.write(bytes);
        }
        finally {
            outStream.close();
        }
    }
}
