package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.enumeration;

import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.DeviceTypeEnumVo;

import java.util.ArrayList;
import java.util.List;

public enum DeviceTypeEnum {
    DEVICE_GROUP("照明群控组"),
    DEVICE_SINGLETON("照明回路");
    /**
     * 描述信息
     */
    private final String description;

    DeviceTypeEnum(String description) {
        this.description = description;
    }

    public static List<DeviceTypeEnumVo> getEnumTypeVo() {
        ArrayList<DeviceTypeEnumVo> enumTypeArr = new ArrayList<>();
        for (DeviceTypeEnum fireinspEnum : DeviceTypeEnum.values()) {
            enumTypeArr.add(new DeviceTypeEnumVo(fireinspEnum.description, fireinspEnum.name()));
        }
        return enumTypeArr;
    }
}
