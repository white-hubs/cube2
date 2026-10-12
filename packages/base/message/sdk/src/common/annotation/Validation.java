package com.guhecloud.cube.base.message.sdk.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Validation {

    String pattern() default "";

    int maxLength() default 0;

    int minLength() default 0;

    double maximum() default Double.MAX_VALUE;

    double minimum() default Double.MIN_VALUE;

    boolean required() default false;
}
