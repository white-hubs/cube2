package com.guhecloud.cube.srv.gen.maintain.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 日常运维应用枚举
 * @date 2023/11/7 11:05
 */
@Getter
public enum MaintainApplicationEnum {
    FAULT("故障单"),
    ELECTRONIC_PATROL("电子巡更"),
    INSPECTION("日常巡检"),
    HIDDENDANGER("隐患单"),
    OUTSOURCE_REPAIR("委外维修"),
    MAINTENACE("维保作业"),
    RETROFIT("更新改造"),
    COMPLAINT("投诉建议"),
    SMALL_ENGINEERING("小散工程"),
    ;

    private final String description;

    MaintainApplicationEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(MaintainApplicationEnum.valueOf(name)) ? "" :
               MaintainApplicationEnum.valueOf(name).getDescription();
    }
}
