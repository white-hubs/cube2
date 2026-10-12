package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 检测属性信息实体类。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("detection_property")
public class Property extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 产品id。
     */
    private Long productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 属性名称。
     */
    private String productPropertyName;

    /**
     * 产品属性code。
     */
    private String productPropertyCode;
    /**
     * 产品属性单位
     */
    private String productPropertyUnit;

    /**
     * 产品属性租户id。
     */
    private Integer productPropertyTenant;

    /**
     * 检测依据id。
     */
    private Long basicId;

    /**
     * 检测标准id。
     */
    private Long standardId;

    /**
     * 阈值上限。
     */
    private BigDecimal maxVal;

    /**
     * 阈值下限。
     */
    private BigDecimal minVal;

}