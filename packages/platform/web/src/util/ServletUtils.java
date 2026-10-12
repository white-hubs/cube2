package com.guhecloud.platform.base.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletUtils {

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ObjectUtils.ifNull(attributes, ServletRequestAttributes::getRequest);
    }

}
