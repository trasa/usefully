package com.meancat.usefully.util;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
    public static byte[] decompress(byte[] message) throws IOException {
        GZIPInputStream gzipIn = new GZIPInputStream(new ByteArrayInputStream(message));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int count;
        while((count=gzipIn.read(buffer)) != -1) {
            outputStream.write(buffer, 0, count);
        }

        gzipIn.close();

        return outputStream.toByteArray();
    }

    public static byte[] compress(byte[] message) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(outputStream);

        gzipOut.write(message);
        gzipOut.finish();

        byte[] result = outputStream.toByteArray();

        gzipOut.close();

        return result;
    }

    public static boolean isGzipped(byte[] bytes) {
        int head = ((int) bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);
        return (GZIPInputStream.GZIP_MAGIC == head);
    }

    public static String compressToBase64(byte[] message) throws IOException {
        return DatatypeConverter.printBase64Binary(compress(message));
    }

    public static byte[] decompressFromBase64(String message) throws IOException {
        byte[] compressed = DatatypeConverter.parseBase64Binary(message);

        if(!isGzipped(compressed)) {
            return null;
        }

        return decompress(compressed);
    }


    /**
     * If this data looks like gzip data, decompress it and return the result.
     * Otherwise just return the data itself.
     *
     * @param data to be examined and, maybe, gunzipped
     * @return data, or unzipped data
     * @throws IOException on fail
     */
    public static byte[] processCompressed(byte[] data) throws IOException {
        return isGzipped(data) ? decompress(data) : data;
    }
}
