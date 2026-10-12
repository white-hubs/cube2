package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 食安数据统计信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("meat_testing_ranking_stat")
public class MeatTestingRankingStatPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 时间范围
     */
    private String timeRange;

    /**
     * 周期开始时间
     */
    private LocalDateTime startDate;

    /**
     * 周期结束时间
     */
    private LocalDateTime endDate;

    /**
     * 检测项目id
     */
    private String meatTestingItem;

    /**
     * 检测项目编号
     */
    private String meatTestingItemCode;

    /**
     * 检测项目名称
     */
    private String meatTestingItemName;

    /**
     * 总检查片数
     */
    private Integer totalCheckNumSum;

    /**
     * 总检查批次
     */
    private Integer totalSum;

    /**
     * 不合格批次数
     */
    private Integer notPassSum;

    /**
     * 上周期检查片数
     */
    private Integer prevTotalCheckNumSum;

    /**
     * 上周期检查批次
     */
    private Integer prevTotalSum;

    /**
     * 上周期不合格批次数
     */
    private Integer prevNotPassSum;

    /**
     * 检查数量环比
     */
    private String checkNumSumRate;

    /**
     * 检查批次环比
     */
    private String totalSumRate;

    /**
     * 不合格数量环比
     */
    private String notPassSumRate;
}
