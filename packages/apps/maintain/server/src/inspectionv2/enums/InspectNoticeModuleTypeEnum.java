package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检通知模块枚举
 *
 * @author chx
 * @date 2023/04/26
 */
@Getter
public enum InspectNoticeModuleTypeEnum {

    /**
     * 任务生成
     */
    TASK_GENERATE("任务生成"),
    /**
     * 任务失效
     */
    TASK_LAPSE("任务失效"),

    /**
     * 任务失效未完成
     */
    TASK_LAPSE_UNFINISHED("任务失效未完成"),

    /**
     * 任务记录异常通知
     */
    TASK_RECORD_ERROR("任务异常通知");

    /**
     * 描述信息
     */
    private final String description;

    InspectNoticeModuleTypeEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(InspectNoticeModuleTypeEnum.valueOf(name)) ? "" :
               InspectNoticeModuleTypeEnum.valueOf(name).getDescription();
    }
}
