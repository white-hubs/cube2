package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 巡检任务状态枚举
 * @date 2023/9/11 14:12
 */
@Getter
public enum InspectionTaskStatusEnum {
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
    STOP("已终止");

    /**
     * 描述信息
     */
    private final String description;

    InspectionTaskStatusEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(InspectionTaskStatusEnum.valueOf(name)) ? "" :
               InspectionTaskStatusEnum.valueOf(name).getDescription();
    }
}
