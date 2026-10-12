package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.listener;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/7 17:45
 * @Description:
 */

@Component
@Slf4j
public class InspectionRequestContextListener implements HandlerInterceptor, WebMvcConfigurer {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (HttpMethod.GET.name().equals(method) || HttpMethod.DELETE.name().equals(method)) {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(ObjectUtils.ifNull(request.getParameter("businessType"), "").trim());
        } else if (HttpMethod.POST.name().equals(method) || HttpMethod.PUT.name().equals(method)) {
            JSONObject jsonObject = null;
            try {
                String businessType = request.getParameter("businessType");
                if (StringUtils.isNotBlank(businessType)) {
                    BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(ObjectUtils.ifNull(request.getParameter("businessType"), "").trim());
                    return true;
                }
                String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                if (StringUtils.isBlank(body)) {
                    return true;
                }
                Object object = JSONObject.parseObject(body, Object.class);
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    if (CollectionUtils.isNotEmpty(jsonArray)) {
                        jsonObject = jsonArray.getJSONObject(0);
                    }
                } else if (object instanceof JSONObject) {
                    jsonObject = JSONObject.parseObject(body);
                }
            } catch (Exception e) {
                log.info("param to json fail");
            }
            if (jsonObject != null) {
                BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(ObjectUtils.ifNull(jsonObject.getString("businessType"), "").trim());
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).order(Integer.MIN_VALUE).addPathPatterns("/**");
    }

}
