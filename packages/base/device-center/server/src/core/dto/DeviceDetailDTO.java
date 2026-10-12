package com.guhecloud.cube.base.device.center.core.dto;

import lombok.Data;

@Data
public class DeviceDetailDTO {
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
    private String deviceBizTypeId;

    /**
     * 归属部门名称
     */
    private String departmentName;

    /**
     * 归属部门ID
     */
    private String departmentId;

    /**
     * 区域ID名称,--分割
     */
    private String areaName;

    /**
     * 区域ID
     */
    private String lastAreaId;

    /**
     * 0 离线 1在线
     */
    private Boolean deviceState;

    /**
     * 管理人员名称,--分割
     */
    private String managerNames;
    /**
     * 管理人员ID,--分割
     */
    private String managerIds;

    /**
     * 三方设备ID
     */
    private String thirdPartyId;

    /**
     * 三方设备配置
     */
    private String thirdpartyCfg;

    /**
     * 扩展信息
     */
    private DeviceExpandDto deviceExpand;

    /**
     * 物联网设备扩展信息
     */
    private DeviceBizIotDTO deviceBizIotDTO;
}
