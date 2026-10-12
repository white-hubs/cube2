package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-25  17:34
 * @Description:
 * @Version: 1.0
 */
@Data
public class DeviceBaseDetailsDTO {
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
     * 产品品类名称
     */
    private String productCategoryName;

    /**
     * 产品品类编号
     */
    private String productCategoryCode;

    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 位置描述信息
     */
    private String locationDes;

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
    private Boolean deviceState;

    /**
     * 0 离线 1在线
     */
    private Integer deviceStateCode;

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
    private Integer tenant;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
