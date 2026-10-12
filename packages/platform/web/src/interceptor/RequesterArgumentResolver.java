package com.guhecloud.platform.base.interceptor;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 客户端请求对象参数转换器
 *
 * @author Woody
 * @date 2021/7/13
 */
@Component
public class RequesterArgumentResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Requester.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory binderFactory
                                 ) throws Exception {
        return SessionContextHolder.getRequester();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this);
    }
}
