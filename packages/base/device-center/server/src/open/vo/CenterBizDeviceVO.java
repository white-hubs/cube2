package com.guhecloud.cube.base.device.center.open.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CenterBizDeviceVO {
    /**
     * 物联网设备ID
     */
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
     * 设备产品ID
     */
    private Long deviceIotProdId;

    /**
     * 归属部门
     */
    private Long departmentId;

    /**
     * 归属部门名称
     */
    private String departmentName;

    /**
     * 区域ID,-分割
     */
    private String areaId;

    /**
     * 区域ID名称,--分割
     */
    private String areaName;

    /**
     * 最末级的区域ID
     */
    private Long areaLastId;

    /**
     * 0 离线 1在线
     */
    private Integer deviceState;

    /**
     * 最后通信时间
     */
    private LocalDateTime communicationTime;

    /**
     * 管理人员ids,-分割
     */
    private String managerIds;

    /**
     * 管理人员名称,--分割
     */
    private String managerNames;

    /**
     * 三方设备ID
     */
    private String thirdPartyId;

    /**
     * 三方设备配置
     */
    private String thirdpartyCfg;

    private Boolean deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer tenant;
    private String createBy;
}
