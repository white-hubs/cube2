package com.guhecloud.platform.base.standard.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.platform.base.standard.jackson.JacksonDecimalSerializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * 小数格式化注解
 *
 * @author Woody
 * @date 2021/8/16
 * @see JacksonDecimalSerializer
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@JacksonAnnotationsInside
@JsonSerialize(using = JacksonDecimalSerializer.class)
public @interface Decimal {
    /**
     * 获取小数保留位数，默认保留2位小数
     *
     * @return 小数保留位数
     */
    int scale() default JacksonDecimalSerializer.DEFAULT_SCALE;

    /**
     * 获取小数舍弃模式，默认四舍五入
     *
     * @return 小数舍弃模式
     */
    RoundingMode mode() default RoundingMode.HALF_UP;
}
