package com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LooseWorkMetricQuery extends BaseStatisticsQuery {
    /**
     * 有问题的，为 null 不过滤
     */
    private Boolean problematic;
}
