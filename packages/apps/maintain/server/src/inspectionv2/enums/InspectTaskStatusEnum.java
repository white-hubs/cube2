package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检任务执行状态
 *
 * @author appet
 * @date 2021/10/16
 */
@Getter
public enum InspectTaskStatusEnum {

    /**
     * 待拆解
     */
    SPLIT("待拆解"),

    /**
     * 待接收
     */
    ACCEPT("待接收"),

    /**
     * 进行中
     */
    EXECUTING("进行中"),

    /**
     * 已完成
     */
    OVER("已完成"),
    /**
     * 已过期
     */
    EXPIRE("已过期"),

    /**
     * STOP
     */
    STOP("已终止"),

    WAIT_REPAIR("待修复"),

    REPAIRED("已修复"),
    ;

    /**
     * 描述信息
     */
    private final String description;

    InspectTaskStatusEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(InspectTaskStatusEnum.valueOf(name)) ? "" :
               InspectTaskStatusEnum.valueOf(name).getDescription();
    }
}
