package com.guhecloud.cube.property.management.client.property.param.property;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 资产明细
 */
@Data
public class PropertyDetailedWorkflowParam {
    private Long id;
    /**
     * 资产编码
     */
    private String serialNumber;
    /**
     * 资产名称
     */
    @NotEmpty(message = "资产名称不能为空")
    private String name;
    /**
     * 资产类别id
     */
    private IdNameParam type;
    /**
     * 购入时间
     */
    @NotNull(message = "购入日期不能为空")
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
    private List adminId;
    /**
     * 区域id
     */
    private Map<String, Object> areaIds;
    private String areaId;

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
     * 财务编码
     */
    private String financeNumber;
}

