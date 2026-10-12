package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 用气安全管理市场卡片
 * @date 2023/9/8 14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UseAirSafetyStatisticsMarketCardVO {
    /**
     * 易燃设备建档数
     */
    private Integer deviceFilingNum;
}
