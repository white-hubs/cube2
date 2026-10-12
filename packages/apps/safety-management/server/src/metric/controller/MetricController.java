package com.guhecloud.cube.srv.gen.safety.management.metric.controller;

import com.guhecloud.cube.srv.gen.safety.management.metric.MetricResult;
import com.guhecloud.cube.srv.gen.safety.management.metric.service.MetricAppService;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController()
public class MetricController {

    private final MetricAppService service;

    public MetricController(MetricAppService service) {
        this.service = service;
    }

    /**
     * 获取指标结果
     *
     * @return 指标结果
     */
    @GetMapping("/metric")
    public MetricResult get(String metric, String payload) {
        return service.get(metric, payload);
    }

    @PostMapping("/metric/batch-get")
    public List<MetricResult> batchGet(@RequestBody List<MetricQuery> query) {
        return Optional.ofNullable(query).orElse(Collections.emptyList()).stream()
                .map(m -> service.get(m.getMetric(), m.getPayload()))
                .collect(Collectors.toList());
    }

    @Data
    public static class MetricQuery {
        private String metric;
        /**
         * payload 必须是标准的 JSONString
         */
        private Object payload;
    }
}
