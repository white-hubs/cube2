package com.guhecloud.platform.base.util;

import com.fasterxml.jackson.core.JsonEncoding;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Http工具类
 *
 * @author Woody
 * @date 2021/6/18
 */
public final class HttpUtils {
    /**
     * 请求结果统一封装响应头
     */
    public static final String UNIFIED_RESPONSE_HEADER = "unified-response-body";

    /**
     * 客户端主机地址请求头数组
     */
    private static final String[] REQUEST_ADDRESS_HEADER_ARRAY = new String[]{
            "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP"
    };

    private HttpUtils() {
    }

    /**
     * 获取Http状态
     *
     * @param code Http状态码
     *
     * @return Http状态枚举
     */
    public static HttpStatus getStatus(Integer code) {
        if (code == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(code);
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * 获取请求域名
     *
     * @param request Http请求对象
     *
     * @return 域名
     */
    public static String getRequestDomain(@NonNull HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() +
               ":" + request.getServerPort() + request.getContextPath();
    }

    /**
     * 获取请求客户端主机地址
     *
     * @param request Http请求对象
     *
     * @return 请求客户端主机地址
     */
    public static String getRequestAddress(@NonNull HttpServletRequest request) {
        // 获取请求主机地址
        String ip = null;
        for (String header : REQUEST_ADDRESS_HEADER_ARRAY) {
            if (!StringUtils.isEmpty(ip = request.getHeader(header)) && !HostUtils.UNKNOWN_ADDRESS.equals(ip)) {
                break;
            }
        }
        if (StringUtils.isEmpty(ip)) {
            ip = request.getRemoteAddr();
        }

        // 如果地址存在多个则获取第一个
        int separatorIndex = ip.indexOf(',');
        if (separatorIndex > -1) {
            ip = ip.substring(0, separatorIndex);
        }

        return HostUtils.DEFAULT_LOCAL_ADDRESS_IPV6.equals(ip) ? HostUtils.LOCAL_ADDRESS : ip;
    }

    /**
     * 初始化请求结果统一封装响应头
     *
     * @param response Http响应对象
     */
    public static void initializeUnifiedResponseHeader(@NonNull HttpServletResponse response) {
        response.setHeader(UNIFIED_RESPONSE_HEADER, String.valueOf(true));
    }

    /**
     * 返回统一封装请求结果
     *
     * @param response Http响应对象
     * @param result   请求结果
     *
     * @throws IOException I/O异常
     */
    public static void writeUnifiedResponse(@NonNull HttpServletResponse response, @NonNull ErrorResponse<?> result)
            throws IOException {
        response.setStatus(result.status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(JsonEncoding.UTF8.getJavaName());
        initializeUnifiedResponseHeader(response);
        OutputStream outputStream = response.getOutputStream();
        JsonUtils.getObjectMapper().writeValue(outputStream, result);
        outputStream.flush();
    }
}
