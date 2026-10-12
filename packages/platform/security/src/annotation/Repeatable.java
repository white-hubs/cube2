package com.guhecloud.platform.security.annotation;

import com.guhecloud.platform.security.interceptor.TokenReplayValidateInterceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口请求是否可以重放（同一个令牌重复请求同一接口），默认支持接口重复请求
 *
 * @author Woody
 * @date 2021/6/20
 * @see TokenReplayValidateInterceptor
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Repeatable {
    /**
     * 判断接口针对同一请求是否可重复调用
     *
     * @return true/false
     */
    boolean value() default true;
}
