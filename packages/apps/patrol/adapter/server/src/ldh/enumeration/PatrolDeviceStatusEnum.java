package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.enumeration;

import lombok.Getter;

/**
 * @author appet
 * @describe 巡更设备的状态
 * 根据对接请款定义设备状态，每个厂家都可能不一样
 * @date 2023/10/19 17:17
 */
@Getter
public enum PatrolDeviceStatusEnum {
    ONLINE("在线"),
    OFFLINE("离线"),
    ;
    private String label;

    PatrolDeviceStatusEnum(String label) {
        this.label = label;
    }

    public static String getCodeByLabel(String label) {
        for (PatrolDeviceStatusEnum status : values()) {
            if (status.getLabel().equals(label)) {
                return status.name();
            }
        }
        return null;
    }

    public static String getLabelByCode(String code) {
        for (PatrolDeviceStatusEnum status : values()) {
            if (status.name().equals(code)) {
                return status.getLabel();
            }
        }
        return null;
    }
}
