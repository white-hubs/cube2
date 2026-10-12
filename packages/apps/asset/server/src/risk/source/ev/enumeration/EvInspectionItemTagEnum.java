package com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检项tag
 */
@Getter
public enum EvInspectionItemTagEnum {
    CHARGE("违规充电"),
    PARKING("违停查处");
    private final String desc;

    EvInspectionItemTagEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(EvInspectionItemTagEnum.valueOf(name)) ? "" :
               EvInspectionItemTagEnum.valueOf(name).getDesc();
    }
}
