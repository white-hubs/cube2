package com.guhecloud.platform.event.src.annotation;

import com.guhecloud.platform.event.src.enumeration.PublisherTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBusListen {

    String key() default "platform_inner";

    PublisherTypeEnum publisherType() default PublisherTypeEnum.ASYNC;

    int order() default Integer.MAX_VALUE;
}
