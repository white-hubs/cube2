package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 资产建档分析
 * @date 2023/9/8 17:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PropertyFilingAnalyseVO {
    /**
     * 资产名称
     */
    private String Name;
    /**
     * 资产数量
     */
    private Integer num;
    /**
     * 资产占比
     */
    private Double ratio;
}
