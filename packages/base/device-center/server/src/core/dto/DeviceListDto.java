package com.guhecloud.cube.base.device.center.core.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DeviceListDto {
    private Long id;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编码(孪生编码)
     */
    private String deviceCode;

    /**
     * 设备业务类型名
     */
    private String deviceBizTypeName;
    /**
     * 设备业务类型ID
     */
    private Long deviceBizTypeId;

    private Long departmentId;

    /**
     * 归属部门名称
     */
    private String departmentName;

    /**
     * 区域id，--分割
     */
    private String areaId;

    /**
     * 区域ID名称,--分割
     */
    private String areaName;

    /**
     * 管理人员ids,-分割
     */
    private String managerIds;

    /**
     * 管理人员名称,--分割
     */
    private String managerNames;

    /**
     * 0 离线 1在线
     */
    private Integer deviceState;

    /**
     * 采购日期
     */
    private LocalDate purchaseDate;

    /**
     * 物联网设备产品ID
     */
    private Long iotDeviceProductId;

    /**
     * 物联网设备产品名称
     */
    private String iotDeviceProductName;

    /**
     * 设备品牌名称
     */
    private String iotDeviceBrandName;

}
