package com.guhecloud.platform.database.annotation;

import com.guhecloud.platform.database.enumeration.ConditionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermissionMoreField {

    String field() default "";

    ConditionEnum condition() default ConditionEnum.EQ;

}
