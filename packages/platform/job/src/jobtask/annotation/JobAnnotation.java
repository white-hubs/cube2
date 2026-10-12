package com.guhecloud.platform.job.jobtask.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解：用于标记可被动态调度的任务方法
 * <p>
 * 功能说明：
 * - 标记在方法上，表示该方法是一个可调度的定时任务
 * - 支持 cron 表达式、任务名称、描述、启用状态控制
 * <p>
 * 使用示例：
 * <pre>
 * ' @JobAnnotation(name = "demoJob", cron = "0/10 * * * * ?", desc = "每10秒执行一次")
 * public void demoTask(String param) {
 *     System.out.println("执行任务: " + param);
 * }
 * </pre>
 * 2025-08-26
 */
@Target(ElementType.METHOD)                    // 作用于方法
@Retention(RetentionPolicy.RUNTIME)           // 运行时保留
@Documented                                   // 可生成 JavaDoc
public @interface JobAnnotation {

    /**
     * 任务唯一名称（必填）
     *
     * @return 任务名称
     */
    String name();

    /**
     * Cron 表达式（必填），用于定义执行周期
     * 示例：0/5 * * * * ? 表示每5秒执行一次
     *
     * @return cron 表达式
     */
    String cron();

    /**
     * 任务描述（可选）
     *
     * @return 描述信息，默认为空字符串
     */
    String desc() default "";

    /**
     * 任务参数（可选）
     *
     * @return 任务参数，默认为空字符串
     */
    String param() default "";

    /**
     * 是否启动该任务（默认不启动）
     *
     * @return true 表示启用，false 表示停用
     */
    boolean enabled() default true;
}
