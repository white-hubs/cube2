package com.guhecloud.cube.srv.gen.safety.management.metric.aop;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Metric {
    /**
     * 指标名，全小写，短横分割命名空间，下划线分割组合描述的单词，如：`safety_management-metric_a`
     *
     * @return 指标名
     */
    String value();

    /**
     * 指标描述
     *
     * @return 指标描述
     */
    String description() default "";

    Class<? extends Annotation> annotation() default Annotation.class;
}
