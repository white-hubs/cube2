package com.guhecloud.platform.database.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据唯一约束注解
 *
 * @author Woody
 * @date 2021/8/4
 * @see com.guhecloud.platform.database.interceptor.UniqueValidateInterceptor
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    /**
     * 获取联合唯一字段数组
     *
     * @return 字段名称数组
     */
    String[] composites() default {};

    /**
     * 违反约束异常信息
     *
     * @return 异常信息
     */
    String message() default "Duplicate value";
}
