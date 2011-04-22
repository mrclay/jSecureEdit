
package org.mrclay.compression;

import org.junit.Test;
import static org.junit.Assert.*;

public class DeflateTest {

    @Test
    public void testRoundTrip() throws Exception {
        System.out.println("roundtrip");
        byte[] input = "Iñtërnâtiônàlizætiøn".getBytes("UTF-8");
        byte[] compressed = Deflate.deflate(input);
        byte[] output = Deflate.inflate(compressed);
        assertArrayEquals(input, output);
    }
}