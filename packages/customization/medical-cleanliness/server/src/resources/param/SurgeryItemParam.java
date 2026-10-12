package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 手术室物品入参
 */
@Data
public class SurgeryItemParam {
    private Long id;
    private Long deviceId;
    private String deviceCode;
    private String deviceName;
    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空！")
    private String name;
    /**
     * 尺寸
     */
    private String size;
    /**
     * 数量
     */
    private String number;
    /**
     * 厂家
     */
    private String manufacturer;
    /**
     * 型号
     */
    private String model;
    /**
     * 机组id
     */
    private Long machinesId;
    /**
     * 机组编号
     */
    private String machinesCode;
}
