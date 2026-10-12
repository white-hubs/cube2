package com.guhecloud.cube.base.dataintegrate.client.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:17
 * @Description:
 */
@Data
public class AlarmMetricQuery extends BaseStatisticsQuery {
    private List<String> applicationCodes;

    private List<String> statusCodes;
}
