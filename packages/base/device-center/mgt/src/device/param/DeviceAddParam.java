package com.guhecloud.cube.iot.mgt.device.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceAddParam {
    @NotNull(message = "产品不能为空")
    private Long productId;
    @NotBlank(message = "设备编号不能为空")
    private String deviceNumber;
    private String deviceName;

    //描述
    private String remark;
}
