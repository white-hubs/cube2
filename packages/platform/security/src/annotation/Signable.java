package com.guhecloud.platform.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口签名注解
 *
 * @author Woody
 * @date 2021/8/30
 */
@Inherited
@Documented
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Signable {
    /**
     * 排除参数名称数组
     *
     * @return 参数名称数组
     */
    String[] exclude() default {};
}
