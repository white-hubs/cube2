package com.guhecloud.cube.property.management.client.property.param.property;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 资产明细
 */
@Data
public class PropertyDetailedParam {
    private Long id;
    /**
     * 资产编码
     */
    private String serialNumber;
    /**
     * 资产名称
     */
    private String name;
    /**
     * 资产类别id
     */
    private Long typeId;
    private String typeName;
    /**
     * 购入时间
     */
    private LocalDateTime purchaseDate;
    /**
     * 维保期限（月）
     */
    private Integer maintenanceTime;
    /**
     * 使用期限（月）
     */
    private Integer usageTime;
    /**
     * 规格型号
     */
    private String norms;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 管理员id
     */
    private Long adminId;
    /**
     * 区域id
     */
    private List<Long> areaIds;
    /**
     * 入库单号
     */
    private String depositoryNumbers;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 原值
     */
    private BigDecimal price;
    /**
     * 存放地点
     */
    private String storageLocation;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 供应商联系方式
     */
    private String supplierPhone;
    /**
     * 供应商联系人
     */
    private String supplierUser;
    /**
     * 维保说明
     */
    private String maintenanceRemarks;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 状态
     */
    private String status;
    /**
     * 资产图片
     */
    private List<Long> imgs;
    /**
     * 资产附件
     */
    private List<Long> file;
    /**
     * 设备编码
     */
    private String deviceNumber;
    /**
     * 来源
     */
    private String sourceId;
    /**
     * 入库时间
     */
    private LocalDateTime startTime;
    /**
     * 资产来源
     */
    private String authority;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 接收部门
     */
    private Long receiveDepId;
    private String receiveDepName;
    /**
     * 接收人
     */
    private Long receiveUserId;
    private String receiveUserPhone;
    /**
     * 财务编码
     */
    private String financeNumber;
    /**
     * 维护部门
     */
    private Long maintenanceDepId;
    private String maintenanceDepName;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地点
     */
    private String location;
}

