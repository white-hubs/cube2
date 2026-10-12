package com.guhecloud.cube.base.videogw.server.common.domain.annotation;

import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoCommandEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 视频命令注解
 *
 * @author lanchun
 * @date 2024/11/27 16:48
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface VideoCommand {

    /**
     * 视频命令
     *
     * @return true/false
     */
    VideoCommandEnum command();

    /**
     * 返回文件地址信息
     *
     * @return
     */
    String resultUrl();
}
