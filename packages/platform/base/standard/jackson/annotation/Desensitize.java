package com.guhecloud.platform.base.standard.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.platform.base.standard.jackson.JacksonDesensitizeSerializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 敏感数据脱敏
 *
 * @author Woody
 * @date 2021/7/12
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@JacksonAnnotationsInside
@JsonSerialize(using = JacksonDesensitizeSerializer.class)
public @interface Desensitize {
    /**
     * 数据脱敏表达式
     *
     * @return 表达式
     */
    String value();
}
