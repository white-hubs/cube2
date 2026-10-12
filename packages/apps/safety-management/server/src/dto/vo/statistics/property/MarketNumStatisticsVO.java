package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 市场相关数量统计
 * @date 2023/9/8 13:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MarketNumStatisticsVO {
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 市场租户号
     */
    private Integer marketTenant;
    /**
     * 数量
     */
    private Long num;
}
