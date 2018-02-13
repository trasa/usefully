package com.meancat.usefully.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZlibUtil {
    public static byte[] deflate(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        byte[] buffer = new byte[1024];
        int count;
        while (!deflater.finished()) {
            count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        return outputStream.toByteArray();
    }


    public static byte[] inflate(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        byte[] buffer = new byte[1024];
        int count;
        while (!inflater.finished()) {
            count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        return outputStream.toByteArray();
    }
}
