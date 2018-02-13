package com.meancat.usefully.util;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A util class to handler compression.
 *
 * @author elvir.bahtijaragic
 */
public final class CompressionUtil {
    private CompressionUtil() {}

    /**
     * Compresses a byte array.
     *
     * @param input bytes to compress
     * @return compressed byte array
     * @throws IOException IOException
     */
    public static byte[] compressByteArray(byte[] input) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(baos);
        final byte[] buffer = new byte[64];
        int n = 0;
        while (-1 != (n = bais.read(buffer))) {
            gzOut.write(buffer, 0, n);
        }
        baos.close();
        gzOut.close();
        return baos.toByteArray();
    }

    /**
     * Decompresses a byte array.
     *
     * @param input compressed byte array
     * @return decompressed bytes
     * @throws IOException on fail
     */
    public static byte[] decompressByteArray(byte[] input) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        BufferedInputStream in = new BufferedInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
        final byte[] buffer = new byte[64];
        int n = 0;
        while (-1 != (n = gzIn.read(buffer))) {
            baos.write(buffer, 0, n);
        }
        baos.close();
        gzIn.close();
        return baos.toByteArray();
    }
}
