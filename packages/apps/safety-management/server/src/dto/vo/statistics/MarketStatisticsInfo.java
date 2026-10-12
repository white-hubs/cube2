package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 巡检任务统计
 * @date 2023/9/20 16:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MarketStatisticsInfo {
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 市场租户code
     */
    private Integer marketTenant;
    /**
     * 任务数
     */
    private Long taskNum;
    /**
     * 环比
     */
    private Double ratio;
}
