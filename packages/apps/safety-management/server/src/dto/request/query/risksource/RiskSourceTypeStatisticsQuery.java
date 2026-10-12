package com.guhecloud.cube.srv.gen.safety.management.dto.request.query.risksource;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 风险源根据类型通用统计查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RiskSourceTypeStatisticsQuery extends BaseStatisticsQuery {
    private List<String> types;
}
