package com.guhecloud.platform.cloud.interceptor;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Feign调用异常处理拦截器
 *
 * @author Woody
 * @date 2021/7/17
 */
@Slf4j
public class FeignExceptionInterceptor implements ErrorDecoder {
    @Override
    public Exception decode(String method, Response response) {
        log.error("Feign interface invoke failed: {}", method);
        // 获取异常信息
        String message;
        try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
            message = Util.toString(reader);
        } catch (IOException e) {
            return e;
        }

        return new ServiceException(String.valueOf(response.status()), message);
    }
}
