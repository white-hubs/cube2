package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 消防安全管理市场统计卡片
 * @date 2023/9/8 14:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FireSafetyStatisticsMarketCardVO {
    /**
     * 消防设备建档数
     */
    private Integer deviceFilingNum;
    /**
     * 消防设备到期数
     */
    private Integer deviceExpireNum;
}
