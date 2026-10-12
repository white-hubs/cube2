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
public enum InspectRecordResultEnum {

    /**
     * 正常
     */
    NORMAL("正常"),

    /**
     * 异常
     */
    ABNORMAL("异常"),
    /**
     * 未知
     */
    UNKNOWN("未知");

    /**
     * 描述信息
     */
    private final String description;

    InspectRecordResultEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(InspectRecordResultEnum.valueOf(name)) ? "" :
               InspectRecordResultEnum.valueOf(name).getDescription();
    }
}
