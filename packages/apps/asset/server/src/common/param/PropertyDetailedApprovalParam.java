package com.guhecloud.cube.srv.gen.property.management.server.common.param;

import annotation.FormField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产明细
 */
@Data
public class PropertyDetailedApprovalParam {
    @FormField(label = "资产id")
    private Long id;
    @FormField(label = "资产编码")
    private String serialNumber;
    @FormField(label = "资产名称")
    private String name;
    @FormField(label = "资产类别id")
    private Long typeId;
    @FormField(label = "资产类别名称")
    private String typeName;
    @FormField(label = "购入时间")
    private LocalDateTime purchaseDate;
    @FormField(label = "维保期限（月）")
    private Integer maintenanceTime;
    @FormField(label = "使用期限（月）")
    private Integer usageTime;
    @FormField(label = "规格型号")
    private String norms;
    @FormField(label = "计量单位")
    private String unit;
    @FormField(label = "数量")
    private Integer number;
    @FormField(label = "原值")
    private BigDecimal price;
    @FormField(label = "管理员id")
    private Long adminId;
    @FormField(label = "区域id")
    private Long areaId;
    @FormField(label = "区域名称")
    private String areaName;
}

