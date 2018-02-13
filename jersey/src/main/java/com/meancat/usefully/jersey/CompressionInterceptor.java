package com.meancat.usefully.jersey;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

public class CompressionInterceptor implements ReaderInterceptor, WriterInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CompressionInterceptor.class);

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        String encoding = context.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);

        if(Strings.isNullOrEmpty(encoding)) {
            return context.proceed();
        }

        String lowerEncoding = encoding.toLowerCase();

        if(lowerEncoding.equals("deflate")) {
            context.setInputStream(new InflaterInputStream(context.getInputStream()));
        } else if(lowerEncoding.equals("gzip")) {
            context.setInputStream(new GZIPInputStream(context.getInputStream()));
        }

        return context.proceed();
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        String encoding = (String) context.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);

        if(Strings.isNullOrEmpty(encoding)) {
            context.proceed();
            return;
        }

        String lowerEncoding = encoding.toLowerCase();

        if(lowerEncoding.equals("deflate")) {
            context.setOutputStream(new DeflaterOutputStream(context.getOutputStream()));
        } else if(lowerEncoding.equals("gzip")) {
            context.setOutputStream(new GZIPOutputStream(context.getOutputStream()));
        }

        context.proceed();
    }
}
