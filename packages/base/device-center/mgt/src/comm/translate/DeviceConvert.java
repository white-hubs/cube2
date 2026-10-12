package com.guhecloud.cube.iot.mgt.comm.translate;

import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.DeviceStatusEnum;

public class DeviceConvert {

    public static String deviceType(int deviceType) {
        switch (deviceType) {
            case 0:
                return "虚拟设备";
            case 1:
                return "真实设备";
            default:
                return "未知设备";
        }
    }

    public static String status(Integer status) {
        Assert.notNull(status, "status is null");

        for (DeviceStatusEnum value : DeviceStatusEnum.values()) {
            if (value.value == status) {
                return value.name;
            }
        }

        return "非法状态";
    }
}
