package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:13
 * @Description: 黑名单阈值配置
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("blacklist_config")
public class BlacklistConfigPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 违停阈值
     */
    private BigDecimal thresholdValue;

    /**
     * 比较条件
     */
    private String comparisonCondition;
}