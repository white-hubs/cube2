package com.guhecloud.platform.base.http.converter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RawInputStreamHttpMessageConverter implements HttpMessageConverter<InputStream> {
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return InputStream.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return InputStream.class.isAssignableFrom(clazz);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return MediaType.parseMediaTypes("*/*");
    }

    @Override
    public void write(InputStream inputStream, MediaType contentType,
                      HttpOutputMessage outputMessage
                     ) throws IOException, HttpMessageNotWritableException {
        StreamUtils.copy(inputStream, outputMessage.getBody());
    }

    @Override
    public InputStream read(Class<? extends InputStream> clazz,
                            HttpInputMessage inputMessage
                           ) throws IOException, HttpMessageNotReadableException {
        return inputMessage.getBody();
    }

}
