package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto;

import com.guhecloud.cube.srv.gen.loose.works.MetricType;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/16 14:04
 * @Description:
 */
@Data
public class LooseWorkNumberMetricParam {

    private Period period;
    private MetricType metricType;
}
