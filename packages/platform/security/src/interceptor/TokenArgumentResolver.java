package com.guhecloud.platform.security.interceptor;

import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 令牌对象参数转换器
 *
 * @author Woody
 * @date 2021/7/13
 */
@Component
public class TokenArgumentResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Jwt.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory binderFactory
                                 ) throws Exception {
        return SecurityContextHolder.getJwt();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this);
    }
}
