package com.guhecloud.cube.biz.metric.server.metric.result.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.repository.dao.MetricResultMapper;
import com.guhecloud.cube.biz.metric.server.metric.result.repository.po.MetricResultPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
public class MetricResultRepositoryImpl implements MetricResultRepository {

    private final MetricResultMapper metricResultMapper;

    public MetricResultRepositoryImpl(MetricResultMapper metricResultMapper) {
        this.metricResultMapper = metricResultMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveMetricResult(MetricResult metricResult) {
        MetricResult metricResultPO = getMetricResultByReqId(metricResult.getMetric(), metricResult.getAppCode(), metricResult.getRequestId());
        if (Objects.isNull(metricResultPO)) {
            MetricResultPO addResult = new MetricResultPO();
            addResult.setMetric(metricResult.getMetric());
            addResult.setAppCode(metricResult.getAppCode());
            addResult.setRequestId(metricResult.getRequestId());
            addResult.setBeginTime(metricResult.getBeginTime());
            addResult.setEndTime(metricResult.getEndTime());
            if (Objects.nonNull(metricResult.getData())) {
                addResult.setData(JsonUtils.serialize(metricResult.getData()));
            }
            return metricResultMapper.insert(addResult) > 0;
        }
        return false;
    }

    @Override
    public MetricResult getMetricResultByReqId(String metric, String appCode, String requestId) {
        List<MetricResultPO> metricResultPOS = this.metricResultMapper.selectList(Wrappers.<MetricResultPO>lambdaQuery()
                                                                                          .eq(StringUtils.isNotBlank(appCode), MetricResultPO::getAppCode, appCode)
                                                                                          .eq(MetricResultPO::getRequestId, requestId)
                                                                                          .eq(MetricResultPO::getMetric, metric));
        if (CollectionUtils.isEmpty(metricResultPOS)) {
            return null;
        }
        MetricResultPO metricResultPO = metricResultPOS.get(0);
        return convertMetricResult(metricResultPO);
    }

    public MetricResult convertMetricResult(MetricResultPO metricResultPO) {
        MetricResult metricResult = new MetricResult();
        metricResult.setMetric(metricResultPO.getMetric());
        metricResult.setAppCode(metricResultPO.getAppCode());
        metricResult.setRequestId(metricResultPO.getRequestId());
        metricResult.setBeginTime(metricResult.getBeginTime());
        metricResult.setEndTime(metricResult.getEndTime());
        metricResult.setData(metricResultPO.getData());
        return metricResult;
    }
}
