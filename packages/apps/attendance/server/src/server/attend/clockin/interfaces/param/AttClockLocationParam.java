package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class AttClockLocationParam {
    /**
     * 规则id
     */
    private Long rulesId;
    /**
     * 地点
     */
    @NotBlank(message = "地点不能为空")
    private String address;
    /**
     * 经度
     */
    @NotBlank(message = "经度不能为空")
    private String longitude;
    /**
     * 维度
     */
    @NotBlank(message = "维度不能为空")
    private String latitude;
    /**
     * 范围
     */
    @NotBlank(message = "范围不能为空")
    private int ranges;
}
