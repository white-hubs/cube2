package com.guhecloud.cube.property.management.client.property.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 安全管理采集的资产数据信息
 * @date 2023/9/13 13:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SafetyManageCollectPropertyVO {
    /**
     * 资产id
     */
    private Long id;
    /**
     * 资产名称
     */
    private String propertyName;
    private String serialNumber;
    /**
     * 资产类别
     */
    private String propertyType;
    /**
     * 入库时间
     */
    private LocalDateTime putInTime;
    /**
     * 维保到期时间
     */
    private LocalDateTime expirationTime;
    /**
     * 资产状态
     */
    private String status;
    /**
     * 管理员Id
     */
    private Long managerId;
    /**
     * 管理员名称
     */
    private String managerName;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 使用人Id
     */
    private Long userId;
    /**
     * 使用人
     */
    private String username;

    private Integer tenant;
    private Boolean deleted;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    /**
     * 数量
     */
    private Integer num;
    private String propertySubType;
    private String propertySubTypeName;
    private String receiveDepName;
    private String maintenanceDepName;
    /**
     * 模型编码
     */
    private String bimCode;
    /**
     * 属性
     */
    private String attribute;
    /**
     * 档位名称
     */
    private String stallName;
    /**
     * 现场点名称
     */
    private String sitePointName;
    /**
     * 维护人
     */
    private Long maintenanceUserId;
    /**
     * 维护人姓名
     */
    private String maintenanceUserName;
    /**
     * 规格型号
     */
    private String norms;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地点
     */
    private String location;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 电动车类型
     */
    private String electronMobileType;
}
