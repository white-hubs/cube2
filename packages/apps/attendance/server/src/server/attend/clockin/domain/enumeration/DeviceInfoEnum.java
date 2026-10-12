package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-03  11:50
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum DeviceInfoEnum {
    DEVICE_TYPE("deviceType", "设备类型"),
    DEVICE_PASSWORD("devicePass", "设备密码"),
    ;
    private final String code;
    private final String remarks;

    DeviceInfoEnum(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }

    public static DeviceInfoEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (DeviceInfoEnum item : DeviceInfoEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
