package com.guhecloud.cube.base.device.center.metadata.annotation;

import com.guhecloud.cube.base.device.center.metadata.lang.From;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.FIELD })
public @interface InvokeForm {

    From[] fromInvoke() default From.CLIENT_BE;

}
