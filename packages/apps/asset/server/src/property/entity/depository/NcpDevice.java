package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-19  11:25
 * @Description:
 * @Version: 1.0
 */
@Data
public class NcpDevice {
    /**
     * ID，自增
     */
    private Long id;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备型号
     */
    private String specification;
    /**
     * 设备品牌
     */
    private String producer;
    /**
     * 设备生产日期
     */
    private LocalDateTime productiondate;
    /**
     * 保质期长度
     */
    private Integer qualitylong;
    /**
     * 保质期单位
     */
    private String qualitydateunit;
    /**
     * 保质期到期时间
     */
    private LocalDateTime qualityLastDate;
    /**
     * 设备状态
     */
    private String status;
    /**
     * 设备状态描述
     */
    private String statusDesc;
    /**
     * 设备使用日期
     */
    private LocalDateTime installDate;
    /**
     * 巡检周期
     */
    private Integer cyclelong;
    /**
     * 巡检周期单位
     */
    private String cycleDateUnit;
    /**
     * 最后巡检时间
     */
    private LocalDateTime cycleLastDate;
    /**
     * 创建时间
     */
    private LocalDateTime createDate;
    /**
     * 创建人编码
     */
    private String createCode;
    /**
     * 创建人姓名
     */
    private String createName;
    /**
     * 维护部门ID
     */
    private Long deptId;
    /**
     * 使用部门ID
     */
    private Integer useDepartMentId;
    /**
     * 设备分类ID
     */
    private Integer deviceCategoryId;
    /**
     * 区域ID
     */
    private String areaId1;
    private String areaId2;
    private String areaId3;
    /**
     * 安装位置
     */
    private String areaAdress;
    /**
     * 计量单位
     */
    private String unitmeasure;
    /**
     * 采购日期
     */
    private LocalDateTime purchasedate;
    /**
     * 资产原值
     */
    private Double assetvalue;
    /**
     * 已折旧额
     */
    private Double depreciation;
    /**
     * 残值
     */
    private Double salvage;
    /**
     * 财务资产编码
     */
    private String financeCode;
    /**
     * 数量
     */
    private Integer deviceCount;

    /**
     * 标准Id
     */
    private Integer standardId;
    private String typeId;
}
