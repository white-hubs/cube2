package com.guhecloud.platform.cloud.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.NonNull;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Feign响应处理拦截器
 *
 * @author Woody
 * @date 2021/7/17
 */
public class FeignResponseInterceptor implements Decoder {
    private final Decoder delegate;

    public FeignResponseInterceptor(@NonNull Decoder delegate, @NonNull ObjectMapper serializer) {
        this.delegate = delegate;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        return this.delegate.decode(response, type);
    }
}
