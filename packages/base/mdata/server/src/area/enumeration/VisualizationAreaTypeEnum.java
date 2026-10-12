package com.guhecloud.cube.biz.mdata.server.area.enumeration;

import lombok.Getter;

@Getter
public enum VisualizationAreaTypeEnum {

    /**
     * 楼栋
     */
    BUILDING("楼栋"),

    /**
     * 楼层
     */
    FLOOR("楼层"),

    /**
     * 其它区域
     */
    OTHER("其它区域");

    private final String desc;

    VisualizationAreaTypeEnum(String desc) {
        this.desc = desc;
    }

    public static VisualizationAreaTypeEnum valueOfDesc(String desc) {
        for (VisualizationAreaTypeEnum typeEnum : values()) {
            if (typeEnum.getDesc().equals(desc)) {
                return typeEnum;
            }
        }
        return null;
    }
}
