package com.guhecloud.cube.biz.meattrade.server.base.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 重量损耗
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeightLoss extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 起点时间
     */
    private Double startTime;

    /**
     * 终点时间
     */
    private Double endTime;

    /**
     * 损耗重量
     */
    private BigDecimal loss;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 备注
     */
    private String remark;
}
