package com.guhecloud.cube.base.dataintegrate.client.param;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:18
 * @Description:
 */
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
