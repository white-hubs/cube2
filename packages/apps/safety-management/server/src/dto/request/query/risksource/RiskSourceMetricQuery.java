package com.guhecloud.cube.srv.gen.safety.management.dto.request.query.risksource;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RiskSourceMetricQuery extends BaseStatisticsQuery {
    private List<String> riskSourceTypes;
}
