package com.guhecloud.platform.cloud.interceptor;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.filter.RepeatableRequestWrapper;
import com.guhecloud.platform.cloud.mq.SysOperationLog;
import com.guhecloud.platform.cloud.mq.SysOperationLogMessage;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestLogInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String interfaceName = extractInterfaceName(request);
        String parameters = extractParameters(request, handler);
        Principal principal = SecurityContextHolder.getPrincipal(true);
        String account = principal != null ? principal.getAccount() : "";
        String ipAddress = request.getHeader("X-Forwarded-For");
        ipAddress = StringUtils.isNotEmpty(ipAddress) ? ipAddress : request.getRemoteAddr();
        logger.info("调用接口：{}，类型：{}，参数：{},账号：{},Ip地址：{}", interfaceName, request.getMethod(), parameters, account, ipAddress);

        // 返回 true 表示继续执行后续的拦截器和处理器，返回 false 则中断请求
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            // 记录异常日志
            logger.error("请求发生异常：" + ex.getMessage());
        } else {
            // 获取返回的状态码
            int statusCode = response.getStatus();
            // 请求处理成功
            logger.info("调用接口：{}，结束;状态码：{}", extractInterfaceName(request), statusCode);
        }
        if (request instanceof RepeatableRequestWrapper && handler instanceof HandlerMethod) {
            if (!request.getMethod().equals(HttpMethod.GET.name())) {
                this.sendMessage(request, response, handler, ex);
            }
        }
    }

    private String extractInterfaceName(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    private String extractParameters(HttpServletRequest request, Object handler) {
        StringBuilder sb = new StringBuilder();

        if (request instanceof RepeatableRequestWrapper && handler instanceof HandlerMethod) {
            if (request.getMethod().equals(HttpMethod.GET.name())) {
                sb = this.getRequestUrlParams(request, sb);
            } else {
                sb = this.getRequestBody(request, sb);
            }
        }
        return sb.substring(0, Math.min(sb.length(), 1000));
    }

    /**
     * 获取request中的body
     *
     * @param request
     * @param sb
     *
     * @return
     */
    private StringBuilder getRequestBody(HttpServletRequest request, StringBuilder sb) {
        if (request instanceof RepeatableRequestWrapper) {
            try {
                BufferedReader reader = request.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
            } catch (Exception e) {

            }
        }
        return sb;
    }

    /**
     * 获取请求上的url参数
     *
     * @param request
     * @param sb
     *
     * @return
     */
    private StringBuilder getRequestUrlParams(HttpServletRequest request, StringBuilder sb) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 遍历请求参数，并将其格式化为字符串
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();

            // 处理多个参数值的情况
            StringBuilder paramValueBuilder = new StringBuilder();
            for (String paramValue : paramValues) {
                paramValueBuilder.append(paramValue).append(",");
            }
            String formattedParamValue = paramValueBuilder.toString();

            // 移除最后一个逗号
            if (formattedParamValue.endsWith(",")) {
                formattedParamValue = formattedParamValue.substring(0, formattedParamValue.length() - 1);
            }

            // 添加到参数字符串中
            sb.append(paramName).append("=").append(formattedParamValue).append("&");
        }

        // 移除最后一个"&"符号
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).order(Integer.MIN_VALUE).addPathPatterns("/**");
    }

    private static final String OK = "OK";
    private static final String STATUS_CODE = "200";
    private static final String FAIL = "FAIL";

    private void sendMessage(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            String ipAddress = request.getHeader("X-Forwarded-For");
            ipAddress = StringUtils.isNotEmpty(ipAddress) ? ipAddress : request.getRemoteAddr();
            SysOperationLog sysOperationLog = new SysOperationLog();
            sysOperationLog.setIpAddress(ipAddress);
            sysOperationLog.setUrl(extractInterfaceName(request));
            sysOperationLog.setStatusCode(String.valueOf(response.getStatus()));
            sysOperationLog.setUserId(SecurityContextHolder.getPrincipal().getId());
            sysOperationLog.setUserName(SecurityContextHolder.getPrincipal().getName());
            sysOperationLog.setMethod(request.getMethod());
            sysOperationLog.setParameters(extractParameters(request, handler));
            sysOperationLog.setResult(STATUS_CODE.equals(sysOperationLog.getStatusCode()) ? OK : FAIL);
            sysOperationLog.setCreateTime(LocalDateTime.now());
            sysOperationLog.setTenant(SessionContextHolder.getRequester().getTenant());
            if (null != ex) {
                sysOperationLog.setAbnormalInfo(ex.getMessage().length() > 200 ? ex.getMessage().substring(0, 200) : ex.getMessage());
            }
            RocketMQContextHolder.send(SysOperationLogMessage.TOPIC, sysOperationLog);
        } catch (Exception e) {
            logger.error("记录操作日志异常:" + e.getMessage());
        }
    }
}
