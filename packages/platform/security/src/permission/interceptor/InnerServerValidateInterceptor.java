package com.guhecloud.platform.security.permission.interceptor;

import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.config.FeignSecurityProperties;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 内部服务调用拦截器
 *
 * @author lanchun
 * @date 2024/6/14 10:11
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InnerServerValidateInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    private final FeignSecurityProperties feignSecurityProperties;

    @PostConstruct
    public void initialize() {
        log.info("Loading inner server validator: {}", this.feignSecurityProperties);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        SecurityContextHolder.setIsInnerServer(Boolean.FALSE);
        String feignInnerInvoke = request.getHeader("feign_inner_invoke");
        //外部服务调用
        if (StringUtils.isEmpty(feignInnerInvoke)) {
            return true;
        }
        try {
            String feignInnerOriginal = AESUtils.decrypt(feignSecurityProperties.getSecretKey(), feignInnerInvoke);
            String[] split = feignInnerOriginal.split("@");
            long time = Long.parseLong(split[1]);
            //过期
            if (System.currentTimeMillis() > time) {
                log.debug("inner_server header Decryption expire {}", feignInnerOriginal);
            } else {
                SecurityContextHolder.setIsInnerServer(Boolean.TRUE);
            }
        } catch (Exception e) {
            log.error("inner_server header Decryption failed,detail message:{}", e);
        }
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).order(Integer.MIN_VALUE).addPathPatterns("/**");
    }
}
