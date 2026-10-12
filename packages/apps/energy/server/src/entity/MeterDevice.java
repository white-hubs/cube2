package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lrj
 * @Description 仪表管理
 * @date 2022-07-04 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterDevice extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //设备区域id
    private String deviceAreaId;
    //设备区域
    private String deviceArea;
    //上级设备
    private String parentName;
    //上级设备id
    private String parentId;
    //上期用量
    private BigDecimal lastReadeValue;
    //本期用量
    private BigDecimal nowReadeValue;
    //本次抄表时间
    private LocalDateTime nowReadeTime;
    //上期抄表时间
    private LocalDateTime lastReadeTime;
    //设备类型id
    private String deviceTypeId;
    //设备名称
    private String deviceName;
    //设备类型
    private String deviceType;
    //设备id
    private String deviceId;
    // 设备品牌(产品信息->品牌名称)
    private String productBrandName;
    // 产品id
    private Long productId;
    // 产品名称
    private String productName;
    // 产品分类全路径
    private String productCategoryPathCode;
    // 产品分类名称
    private String productCategoryName;
    /**
     * 仪表类型
     */
    private String instrumentType;
    /**
     * 仪表编码
     */
    private String code;
    //本期读数
    private BigDecimal readeValue;
    //初始读数
    private BigDecimal initValue;
    // 最后一级区域
    private Long lastAreaId;
    private String lastAreaName;

}