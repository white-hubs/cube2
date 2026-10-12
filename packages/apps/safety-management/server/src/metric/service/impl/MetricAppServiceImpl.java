package com.guhecloud.cube.srv.gen.safety.management.metric.service.impl;

import com.guhecloud.cube.srv.gen.safety.management.metric.MetricResult;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.metric.service.MetricAppService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class MetricAppServiceImpl implements MetricAppService, BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(MetricAppServiceImpl.class);

    private final Map<String, MetricMethod> metrics = new HashMap<>();

    private final ApplicationContext context;

    public MetricAppServiceImpl(ApplicationContext context) {
        this.context = context;
    }

    /**
     * 初始化时扫描所有指标方法并缓存
     */
    @PostConstruct
    public void init() {
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(MetricService.class);
        for (Object bean : beansWithAnnotation.values()) {
            Class<?> beanClass = AopUtils.getTargetClass(bean);
            for (Method method : beanClass.getDeclaredMethods()) {
                log.info("scan metric {} name {}", beanClass.getSimpleName(), method.getName());
                if (!method.isAnnotationPresent(Metric.class)) {
                    continue;
                }
                Metric metric = method.getAnnotation(Metric.class);
                MetricMethod metricMethod = MetricMethod.builder()
                        .metric(metric)
                        .method(method)
                        .context(beanClass)
                        .build();
                metrics.put(metric.value(), metricMethod);
            }
        }
    }

    @Override
    public MetricResult get(String metricName, Object payload) {
        MetricMethod metricMethod = Optional.ofNullable(metrics.get(metricName))
                .orElseThrow(() -> new ServiceException("metric not found"));
        Class<?> beanCtx = metricMethod.context;
        Method method = metricMethod.method;
        Parameter[] parameters = method.getParameters();
        Object[] args = Arrays.stream(parameters)
                .map(parameter -> {
                    if (parameter.isAnnotationPresent(MetricPayload.class)) {
                        return covertPayload(payload, parameter);
                    }
                    return null;
                })
                .toArray();
        MetricResult.MetricResultBuilder<?, ?> builder = MetricResult.builder();
        builder.description(metricMethod.metric.description());
        builder.metric(metricMethod.metric.value());
        try {
            return builder
                    .result(method.invoke(context.getBean(beanCtx), args))
                    .successful(true)
                    .build();
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            log.warn("get metric {} result failed!", metricName, e);
            return builder
                    .successful(false)
                    .result(null)
                    .build();
        }
    }

    private Object covertPayload(Object payload, Parameter target) {
        if (Objects.isNull(payload)) {
            return null;
        }
        if (payload instanceof String && StringUtils.isBlank(payload.toString())) {
            return null;
        }
        if (payload instanceof String && StringUtils.isNotBlank(payload.toString())) {
            return JsonUtils.deserialize(payload.toString(), target.getType());
        }
        return JsonUtils.deserialize(payload, target.getType());
    }

    @Getter
    @SuperBuilder
    protected static class MetricMethod {
        private Metric metric;

        private Method method;

        private Class<?> context;
    }
}
