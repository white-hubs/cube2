package com.guhecloud.cube.biz.metric.server.metric.result.service;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.repository.MetricResultRepository;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@EventBusListen(order = 1)
public class MetricService {

    private final Map<String, Method> metrics = new HashMap<>();

    private final ApplicationContext applicationContext;

    private final MetricResultRepository metricResultRepository;

    public MetricService(ApplicationContext applicationContext, MetricResultRepository metricResultRepository) {
        this.applicationContext = applicationContext;
        this.metricResultRepository = metricResultRepository;
    }

    @Subscribe
    @AllowConcurrentEvents
    private void initHandler(SysReadEvent event) {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Service.class);
        for (Object bean : beansWithAnnotation.values()) {
            Class<?> clazz = AopUtils.getTargetClass(bean);
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(MetricTopic.class)) {
                    method.setAccessible(true);
                    MetricTopic metricTopic = method.getAnnotation(MetricTopic.class);
                    metrics.put(metricTopic.topic(), method);
                }
            }
        }
    }

    public MetricResult getMetricResult(String metric, MetricParam param) {
        if (!this.metrics.containsKey(metric)) {
            throw new ServiceException("metric not found");
        }
        Method metricMethod = this.metrics.get(metric);
        Object bean = applicationContext.getBean(metricMethod.getDeclaringClass());
        // 获取方法参数类型
        Class<?>[] parameterTypes = metricMethod.getParameterTypes();
        try {
            Object adaptedParam = parameterTypes[0].getDeclaredConstructor().newInstance();
            // 复制属性
            ObjectUtils.copying(param, adaptedParam);
            Object result = metricMethod.invoke(bean, adaptedParam);
            MetricResult metricResult = new MetricResult();
            if (Objects.nonNull(param.getPeriod())) {
                metricResult.setBeginTime(param.getPeriod().getBegin());
                metricResult.setEndTime(param.getPeriod().getEnd());
            }
            metricResult.setAppCode(param.getAppCode());
            metricResult.setRequestId(param.getRequestId());
            metricResult.setMetric(metric);
            metricResult.setData(result);
            metricResultRepository.saveMetricResult(metricResult);
            return metricResult;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            log.warn("get metric failed", e);
            throw new RuntimeException(e);
        }
    }
}
