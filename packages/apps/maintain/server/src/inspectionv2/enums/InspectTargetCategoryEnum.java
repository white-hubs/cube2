package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检对象分类
 *
 * @author appet
 * @date 2021/10/16
 */
@Getter
public enum InspectTargetCategoryEnum {
    /**
     * 档位
     */
    STALL("档位"),

    /**
     * 设备
     */
    DEVICE("设备"),
    /**
     * 资产
     */
    PROPERTY("资产"),
    /**
     * 现场点
     */
    SITE_POINT("现场点"),
    /**
     * 风险源
     */
    RISK_SOURCE("风险源"),

    /**
     * 小散工程
     */
    SMALL_ENGINEERING("小散工程"),
    ;

    /**
     * 描述信息
     */
    private final String description;

    InspectTargetCategoryEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {

        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(InspectTargetCategoryEnum.valueOf(name)) ? "" :
               InspectTargetCategoryEnum.valueOf(name).getDescription();
    }

    public static InspectTargetCategoryEnum getTargetCategoryEnum(String name) {

        if (ObjectUtils.isEmpty(name)) {
            return null;
        }
        for (InspectTargetCategoryEnum value : InspectTargetCategoryEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
