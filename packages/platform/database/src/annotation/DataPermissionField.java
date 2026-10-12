package com.guhecloud.platform.database.annotation;

import com.guhecloud.platform.database.enumeration.ConditionEnum;
import com.guhecloud.platform.database.enumeration.OpConditionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermissionField {
    String permission() default "";

    String field() default "";

    DataPermissionMoreField[] fields() default {};

    ConditionEnum condition() default ConditionEnum.EQ;

    OpConditionEnum opCondition() default OpConditionEnum.OR;

    Class<? extends FieldFilter> fieldFilter() default DefaultFieldFilter.class;

    Class<? extends FieldParamConvert> fieldParamConvert() default DefaultFieldParamConvert.class;

    interface FieldFilter {

        Object getFieldFilterParams();

    }

    class DefaultFieldFilter implements FieldFilter {

        @Override
        public Object getFieldFilterParams() {
            return null;
        }
    }

    interface FieldParamConvert {
        Object getFieldParamConvert(Object filterParams);
    }

    class DefaultFieldParamConvert implements FieldParamConvert {

        @Override
        public Object getFieldParamConvert(Object filterParams) {
            return null;
        }
    }

}

