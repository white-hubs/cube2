package com.guhecloud.platform.database.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 机密数据注解，使用该注解的字段值将会在入库前加密和出库后解密
 *
 * @author Woody
 * @date 2021/7/8
 * @see com.guhecloud.platform.database.interceptor.PropertyUpdateInterceptor
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface Confidential {
    /**
     * 判断数据是否是机密数据
     *
     * @return true/false
     */
    boolean value() default true;
}
