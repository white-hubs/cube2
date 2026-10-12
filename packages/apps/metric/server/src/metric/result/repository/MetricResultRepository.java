package com.guhecloud.cube.biz.metric.server.metric.result.repository;

import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MetricResult;

public interface MetricResultRepository {

    Boolean saveMetricResult(MetricResult metricResult);

    MetricResult getMetricResultByReqId(String metric, String appCode, String requestId);

}
