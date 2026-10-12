package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class AttClockDeviceParam {
    private Long id;
    /**
     * 规则id
     */
    private Long rulesId;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备类型
     */
    private String genreName;
    /**
     * 设备型号
     */
    private String modelName;
    /**
     * 设备编码
     */
    @NotBlank(message = "设备编码不能为空")
    private String code;
}
