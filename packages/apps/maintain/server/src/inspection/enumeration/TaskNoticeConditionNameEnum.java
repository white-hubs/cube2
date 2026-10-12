package com.guhecloud.cube.srv.gen.maintain.server.inspection.enumeration;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import lombok.Getter;
import lombok.NonNull;

/**
 * 任务通知模块名称枚举
 * 注意不是模块的枚举是模块name的枚举
 */
@Getter
public enum TaskNoticeConditionNameEnum {

    /**
     * 任务生成
     */
    GENERATE("任务生成"),
    /**
     * 任务失效
     */
    LAPSE("任务失效"),

    /**
     * 任务失效未完成
     */
    UNFINISHED("任务失效未完成");

    /**
     * 描述信息
     */
    private final String description;

    TaskNoticeConditionNameEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {
        return InspectTaskStatusEnum.getDescription(name);
    }
}
