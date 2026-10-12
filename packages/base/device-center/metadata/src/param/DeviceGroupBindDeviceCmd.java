package com.guhecloud.cube.base.device.center.metadata.param;

import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterParam;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class DeviceGroupBindDeviceCmd {

    /**
     * 分组id
     */
    @NotNull(message = "分组id不能为空")
    private Long id;

    /**
     * 设备过滤参数
     */
    private DeviceFilterParam deviceFilterParam;

    public static DeviceGroupBindDeviceCmd of(Long id, DeviceFilterParam deviceFilterParam) {
        DeviceGroupBindDeviceCmd param = new DeviceGroupBindDeviceCmd();
        param.setId(id);
        param.setDeviceFilterParam(deviceFilterParam);
        return param;
    }

}
