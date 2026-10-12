package com.guhecloud.kaihe.scada.server.infrastructure.web.response;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.kaihe.scada.server.infrastructure.dto.ConvertionDTO;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashMap;
import java.util.Map;

public class UnifiedResponseHandler implements HandlerMethodReturnValueHandler {

    private final ObjectMapper objectMapper;

    public static String DEFAULT_IGNORE_HEADER = "ignore-unified-response";

    // 如果包含特殊的请求头则不进行统一格式的封装
    private final String ignoreHeader = DEFAULT_IGNORE_HEADER;

    public UnifiedResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return hasResponseBodyAnnotation(returnType) && hasUnifiedResponseBodyAnnotation(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest
                                 ) throws Exception {
        mavContainer.setRequestHandled(true);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (StringUtils.isEmpty(webRequest.getHeader(ignoreHeader))) {
            if (returnValue instanceof Result) {
                Map<String, Object> result = parseResult((Result) returnValue);
                objectMapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8).writeObject(result);
            } else {
                ResponseResult result = ResponseResult.builder().success(true).data(isDtoWithNullTarget(returnValue) ? null : returnValue)
                        .build();
                objectMapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8).writeObject(result);
            }
        } else {
            objectMapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8)
                    .writeObject(isDtoWithNullTarget(returnValue) ? null : returnValue);
        }
        response.getOutputStream().flush();
    }

    private boolean hasResponseBodyAnnotation(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(ResponseBody.class) || returnType.getDeclaringClass().isAnnotationPresent(ResponseBody.class)
               || returnType.getDeclaringClass().isAnnotationPresent(RestController.class);
    }

    private boolean hasUnifiedResponseBodyAnnotation(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(UnifiedResponseBody.class) ||
               returnType.getDeclaringClass().isAnnotationPresent(UnifiedResponseBody.class);
    }

    private boolean isDtoWithNullTarget(Object returnValue) {
        if (returnValue instanceof ConvertionDTO) {
            return ((ConvertionDTO<?, ?>) returnValue).isNullTarget();
        }
        return false;
    }

    private Map<String, Object> parseResult(Result result) {
        Map<String, Object> finalResult = new HashMap<>();
        finalResult.put("data", result.getData());
        finalResult.put("message", result.getMessage());
        finalResult.put("success", result.getSuccess());
        finalResult.putAll(result.getMap());
        return finalResult;
    }

}
