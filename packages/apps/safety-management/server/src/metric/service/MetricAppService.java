package com.guhecloud.cube.srv.gen.safety.management.metric.service;

import com.guhecloud.cube.srv.gen.safety.management.metric.MetricResult;
import com.guhecloud.platform.base.standard.exception.ServiceException;

public interface MetricAppService {
    /**
     * 获取指标结果
     *
     * @param metricName 指标名
     * @param payload    查询载荷
     *
     * @return 指标结果
     */
    MetricResult get(String metricName, Object payload) throws ServiceException;
}
