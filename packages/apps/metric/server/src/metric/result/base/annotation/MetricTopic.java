package com.guhecloud.cube.biz.metric.server.metric.result.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指标注解
 *
 * @author lanchun
 * @date 2025/6/4 10:59
 * <p>
 * Need use MetricService annotation
 * @see MetricService
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface MetricTopic {

    /**
     * 指标主题
     *
     * @return 指标主题
     */
    String topic();

    /**
     * 描述
     *
     * @return 指标描述
     */
    String description();
}
