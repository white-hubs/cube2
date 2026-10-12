package annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户表单配置使用
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormField {
    //标题
    String label();

    //忽略
    boolean ignore() default false;

    //是否必输
    boolean required() default false;

    //表单类型
    String fromType() default "";
}
