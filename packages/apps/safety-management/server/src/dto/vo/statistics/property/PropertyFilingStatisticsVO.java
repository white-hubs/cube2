package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 资产建档统计
 * @date 2023/9/13 19:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PropertyFilingStatisticsVO {
    /**
     * 易燃设备总数
     */
    private Integer flammableEquipmentNum;
    /**
     * 电动车设备数
     */
    private Integer electricVehicleNum;
    /**
     * 消防设备总数
     */
    private Integer fireFightingEquipmentNum;
    /**
     * 消防设备过期数
     */
    private Integer fireFightingEquipmentExpiredNum;
    /**
     * 小散工程数量
     */
    private Integer looseWorksNum;
}
