package com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zengyadong@guhecloud.com
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField {
    /**
     * 标题
     */
    String value();

    /**
     * 日期格式, 如: yyyy-MM-dd
     */
    String dateFormat() default "";

    /**
     * 是否不为空：默认false（用于导入校验）
     */
    boolean isNotNull() default false;

    /**
     * 最大长度（用于导入校验）
     */
    int maxLength() default 0;

    /**
     * 配置key
     */
    String configKey() default "";

}
