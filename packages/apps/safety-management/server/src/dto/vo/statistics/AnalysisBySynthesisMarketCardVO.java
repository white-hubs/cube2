package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 综合分析市场统计卡片
 * @date 2023/9/8 16:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnalysisBySynthesisMarketCardVO {
    /**
     * 风险指数
     */
    private Integer riskIndex;
    /**
     * 易燃设备建档数
     */
    private Integer flammableEquipmentNum;
    /**
     * 小散工程数
     */
    private Integer powderProjectNum;
    /**
     * 消防设备建档数
     */
    private Integer fireFightingEquipmentFilingNum;
    /**
     * 消防设备到期数
     */
    private Integer fireFightingEquipmentExpireNum;
    /**
     * 电动车建档数
     */
    private Integer electricVehicleFilingNum;
}
