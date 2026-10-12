package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Data
public class DeviceBaseAndIotPropertyInfoQry {

    /**
     * 标签编号
     */
    @NotBlank(message = "标签编号不能为空")
    private String tagCode;

    /**
     * iot属性编号集合
     */
    @NotNull(message = "iot属性编号不能为空")
    @Size(min = 1, max = 20, message = "属性编号限制个数为1-20之间")
    private Set<String> iotPropertyCodes;
}
