package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseStatisticsQuery {
    /**
     * 创建时间区间
     */
    private Period createdPeriod;

    /**
     * 统计的市场范围
     */
    private List<String> marketTenantIDs;

    /**
     * 巡检业务类型
     */

    private String businessType;
}
