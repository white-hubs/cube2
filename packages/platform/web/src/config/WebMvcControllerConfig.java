package com.guhecloud.platform.base.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

/**
 * 兼容处理spring6.x中get请求使用对象接收参数时，ExtendedServletRequestDataBinder会自动从请求头绑定到数据类的同名属性中
 * 处理资料:https://github.com/spring-projects/spring-framework/issues/34125
 */
@ControllerAdvice
public class WebMvcControllerConfig {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder instanceof ExtendedServletRequestDataBinder) {
            ((ExtendedServletRequestDataBinder) binder).addHeaderPredicate(header -> false);
        }
    }
}
