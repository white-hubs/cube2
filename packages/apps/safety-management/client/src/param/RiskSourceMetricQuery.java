package com.guhecloud.cube.base.dataintegrate.client.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 17:02
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RiskSourceMetricQuery extends BaseStatisticsQuery {
    private List<String> riskSourceTypes;
}

