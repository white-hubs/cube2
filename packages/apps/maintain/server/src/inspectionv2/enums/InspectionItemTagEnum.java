package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检项tag
 */
@Getter
public enum InspectionItemTagEnum {
    CHARGE("违规充电"),
    PARKING("违停查处"),
    ;
    private final String desc;

    InspectionItemTagEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(InspectionItemTagEnum.valueOf(name)) ? "" :
               InspectionItemTagEnum.valueOf(name).getDesc();
    }
}
