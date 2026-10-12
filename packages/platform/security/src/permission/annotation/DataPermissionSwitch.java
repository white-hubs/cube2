package com.guhecloud.platform.security.permission.annotation;

import com.guhecloud.platform.base.util.StringUtils;

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
public @interface DataPermissionSwitch {

    boolean enable() default true;

    String dataModel() default StringUtils.EMPTY_STRING;

    String tables() default StringUtils.EMPTY_STRING;
}
