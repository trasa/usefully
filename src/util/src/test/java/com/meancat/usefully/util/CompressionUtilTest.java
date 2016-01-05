package com.meancat.usefully.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class CompressionUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(CompressionUtilTest.class);

    @Test
    public void compressAndDecompress() throws IOException {
        String original = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent varius, ante in pulvinar aliquet, turpis quam ultrices nisi, egestas ultricies orci nisi ac leo. Donec ultrices, sapien sed sodales fringilla, sem tellus vulputate magna, eget gravida massa dui quis sapien. Phasellus non viverra lorem. Nunc sollicitudin elit sed dignissim porttitor. Phasellus commodo massa dolor, ac volutpat purus fringilla suscipit. Maecenas feugiat ligula vel mi aliquam, eu accumsan elit pulvinar. Sed nisi risus, sodales sit amet blandit quis, sollicitudin a erat. Curabitur arcu ipsum, tincidunt ac gravida eu, tempus quis ante. Suspendisse potenti. Aenean blandit, dolor non eleifend molestie, odio purus faucibus ante, eget vulputate lacus tellus non quam. Integer ornare arcu tellus, ut feugiat quam tempor vel. Mauris quis faucibus erat, id molestie nisi. Ut nec libero ligula.";

        byte[] compressed = CompressionUtil.compressByteArray(original.getBytes());
        logger.info("Original size {}, compressed size {}", original.length(), compressed.length);

        byte[] decompressed = CompressionUtil.decompressByteArray(compressed);

        assertEquals(original.length(), decompressed.length);
        String s = new String(decompressed, Charset.defaultCharset());
        assertEquals(original, s);
    }
}
