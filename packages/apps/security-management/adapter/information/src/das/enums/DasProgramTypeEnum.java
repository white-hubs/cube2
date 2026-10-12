package com.guhecloud.cube.srv.gen.adapter.information.server.das.enums;

import lombok.Getter;

/**
 * 节目类型
 */
@Getter
public enum DasProgramTypeEnum {
    //n代表普通节目，l代表复合节目，i表示互动节目
    NORMAL("n", "普通节目"),
    COMPLEX("l", "复合节目"),
    INTERACTIVE("i", "互动节目");

    private String type;
    private String desc;

    DasProgramTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static DasProgramTypeEnum getByType(String type) {
        for (DasProgramTypeEnum value : DasProgramTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

    public static String getDescByType(String type) {
        DasProgramTypeEnum byType = getByType(type);
        return byType == null ? null : byType.getDesc();
    }
}
