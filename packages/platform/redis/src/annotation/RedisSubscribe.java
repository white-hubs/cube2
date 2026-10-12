package com.guhecloud.platform.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisSubscribe {

    /**
     * 订阅的事件主题
     *
     * @return
     */
    String topic();
}
