package com.guhecloud.platform.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.platform.base.model.ErrorResponse;
import com.guhecloud.platform.base.standard.exception.ClientException;
import com.guhecloud.platform.base.standard.exception.PermissionDeniedException;
import com.guhecloud.platform.base.standard.exception.ResourceConflictException;
import com.guhecloud.platform.base.standard.exception.ResourceNotAcceptableException;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.standard.exception.TooManyRequestException;
import com.guhecloud.platform.base.standard.exception.UnauthenticatedException;
import com.guhecloud.platform.base.standard.exception.UnimplementedException;
import com.guhecloud.platform.base.util.JsonUtils;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.InputStream;

public class CustomFeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream body = response.body().asInputStream()) {
            var json = new String(body.readAllBytes());
            var error = JsonUtils.deserialize(json);
            if (error != null) {
                var standardError = parseErrorResponse(response.status(), error);
                return toServiceException(standardError);
            }
        } catch (Exception ignored) {
            // ignore 走最后的 return
        }
        return new ServiceException("远程服务调用异常", response.status() + "", response.reason());
    }

    private ServiceException toServiceException(ErrorResponse<?> error) {
        return switch (error.status()) {
            case 400 -> new ClientException(error.message(), error.code(), error.details());
            case 401 -> new UnauthenticatedException(error.message(), error.code(), error.details());
            case 403 -> new PermissionDeniedException(error.message(), error.code(), error.details());
            case 404 -> new ResourceNotFoundException(error.message(), error.code(), error.details());
            case 406 -> new ResourceNotAcceptableException(error.message(), error.code(), error.details());
            case 409 -> new ResourceConflictException(error.message(), error.code(), error.details());
            case 412 -> new ResourcePreconditionFailedException(error.message(), error.code(), error.details());
            case 429 -> new TooManyRequestException(error.message(), error.code(), error.details());
            case 501 -> new UnimplementedException(error.message(), error.code(), error.details());
            default -> new ServiceException(error.message(), error.code(), error.details());
        };
    }

    private ErrorResponse<?> parseErrorResponse(int httpStatus, JsonNode json) {
        // 规范后的异常格式
        if (json.has("message") && json.has("code") && json.has("details") && json.has("timestamp")) {
            var message = json.get("message").asText();
            var code = json.get("code").asText();
            var details = json.get("details").toString();
            var timestamp = json.get("timestamp").asLong();
            return new ErrorResponse<>(httpStatus, code, message, details, timestamp);
        }
        // 规范前的异常格式
        if (json.has("data") && json.has("code") && json.has("message") && json.has("errorCode") && json.has("errorDetails")) {
            var message = json.get("message").asText();
            // 条件中的 code 实际上是 httpStatus，仅用于识别是否是老版异常，不作为业务异常码使用
            var code = json.get("errorCode").asText();
            var details = json.get("errorDetails").toString();
            return ErrorResponse.of(httpStatus, code, message, details);
        }
        return ErrorResponse.ofSimpleMessage(httpStatus, json.toString());
    }
}
