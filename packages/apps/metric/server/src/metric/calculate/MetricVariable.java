package com.guhecloud.cube.biz.metric.server.metric.calculate;

import com.guhecloud.cube.biz.metric.server.metric.calculate.evaluator.MetricResultCache;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.MetricService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MetricVariable implements Variable {

    private final MetricService metricService;

    private final MetricResultCache metricResultCache;

    public MetricVariable(MetricService metricService, MetricResultCache metricResultCache) {
        this.metricService = metricService;
        this.metricResultCache = metricResultCache;
    }

    @Override
    public Object getVariableValue(String key, Object param) {
        MetricParam metricParam = (MetricParam) param;
        Object o = this.metricResultCache.get(key, metricParam);
        if (Objects.isNull(o)) {
            o = this.metricService.getMetricResult(key, metricParam);
            metricResultCache.put(key, metricParam, o);
        }
        MetricResult metricResult = (MetricResult) o;
        return metricResult.getData();
    }
}
