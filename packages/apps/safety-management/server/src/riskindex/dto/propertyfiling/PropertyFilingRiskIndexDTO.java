package com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.propertyfiling;

import com.guhecloud.cube.srv.gen.safety.management.enumeration.PropertyCategoryEnum;
import com.guhecloud.cube.srv.gen.safety.management.riskindex.dto.RiskIndexDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 风险源建档情况指标
 *
 * @author lanchun
 * @date 2023/11/6 15:45
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PropertyFilingRiskIndexDTO extends RiskIndexDTO {

    /**
     * 建档类型
     */
    private List<PropertyCategoryRiskIndex> propertyCategoryDetails;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PropertyCategoryRiskIndex {
        private PropertyCategoryEnum propertyCategory;

        private int riskIndex;

    }
}
