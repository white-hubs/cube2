package com.guhecloud.cube.biz.ouaa.server.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class HttpServletRequestUtil {
    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    public static Map<String, String> getParametersAsSingleValueMap(HttpServletRequest request) {
        return getParameters(request).toSingleValueMap();
    }
}
