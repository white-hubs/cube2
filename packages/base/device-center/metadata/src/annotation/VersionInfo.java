package com.guhecloud.cube.base.device.center.metadata.annotation;

import com.guhecloud.cube.base.device.center.metadata.lang.Version;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.FIELD })
public @interface VersionInfo {
    Version availableSince() default Version.V_2_0_1; // 默认可用版本

    Version deprecatedSince() default Version.V_2_0_1;   // 默认为空，表示未弃用

    Version removedSince() default Version.V_2_0_1;      // 默认为空，表示未移除

    Version plannedSince() default Version.V_2_1_1;      // 默认为空，表示未计划

    String plannedRemove() default "";
}