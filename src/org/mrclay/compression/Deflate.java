
package org.mrclay.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Deflate {
    /**
     * deflate a byte array
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] deflate(byte[] input) throws IOException {
        Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION);
        compressor.setInput(input);

        // mark the end of the input to be compressed
        compressor.finish();

        ByteArrayOutputStream balloon = new ByteArrayOutputStream(input.length);
        byte[] buffer = new byte[1024];
        int count;
        while (! compressor.finished()) {
            count = compressor.deflate(buffer);
            balloon.write(buffer, 0, count);
        }
        balloon.close();
        return balloon.toByteArray();
    }

    /**
     * inflate a byte array
     * @param input
     * @return
     */
    public static byte[] inflate(byte[] input) {
        Inflater expander = new Inflater();
        expander.setInput(input);
        ByteArrayOutputStream balloon = new ByteArrayOutputStream(input.length);
        byte[] buffer = new byte[1024];
        int bytesRead;
        try {
            while (true) {
                bytesRead = expander.inflate(buffer);
                if (bytesRead > 0) {
                    balloon.write(buffer, 0, bytesRead);
                } else if (bytesRead == 0 && expander.finished()) {
                    break;
                } else {
                    throw new RuntimeException("bad zip data, size:" + input.length);
                }
            }
        } catch (DataFormatException t) {
            throw new RuntimeException(t);
        } finally {
            expander.end();
        }
        return balloon.toByteArray();
    }
}
