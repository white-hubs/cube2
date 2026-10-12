package com.guhecloud.cube.biz.metric.server.metric.calculate;

import com.guhecloud.cube.biz.metric.server.metric.calculate.dto.CalculateResult;
import com.guhecloud.cube.biz.metric.server.metric.calculate.evaluator.MvelExpressionEvaluator;
import com.guhecloud.cube.biz.metric.server.metric.library.service.query.MetricLibraryQueryService;
import com.guhecloud.cube.biz.metric.server.metric.library.vo.MetricLibraryVO;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.score.param.CalculateParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MetricCalculate extends MvelExpressionEvaluator {

    private final MetricLibraryQueryService metricLibraryQueryService;

    public MetricCalculate(MetricLibraryQueryService metricLibraryQueryService,
                           MetricVariable metricVariable
                          ) {
        super(metricVariable);
        this.metricLibraryQueryService = metricLibraryQueryService;
    }

    /**
     * 指标表达式计算
     *
     * @param param
     *
     * @return
     */
    public CalculateResult calculate(CalculateParam param) {
        MetricLibraryVO metricLibraryDetail = metricLibraryQueryService.getMetricLibraryDetail(param.getComputeMetric());
        Map<String, String> metricVariables = getMetricVariables(metricLibraryDetail);
        double evaluate = 0.0;
        try {
            evaluate = evaluate(metricLibraryDetail.getExecutableFormula(), metricVariables, convertParam(param));
        } catch (Exception e) {
            log.error("规则计算失败", e);
        }
        Map<String, Object> metricMetaData = loadVariablesValues(metricVariables, convertParam(param));
        return CalculateResult.builder()
                .metricId(metricLibraryDetail.getId())
                .metricCode(metricLibraryDetail.getCode())
                .metricName(metricLibraryDetail.getName())
                .result(evaluate)
                .metricMetaData(metricMetaData)
                .build();
    }

    /**
     * 指标表达式结果
     *
     * @param param
     *
     * @return
     */
    public Object getEvaluate(CalculateParam param) {
        MetricLibraryVO metricLibraryDetail = metricLibraryQueryService.getMetricLibraryDetail(param.getComputeMetric());
        Map<String, String> metricVariables = getMetricVariables(metricLibraryDetail);
        Object evaluate = getEvaluate(metricLibraryDetail.getExecutableFormula(), metricVariables, convertParam(param));
        if (evaluate instanceof Number) {
            double evaluateNumber = 0.0;
            try {
                evaluateNumber = convertResult(evaluate);
            } catch (Exception e) {
                log.error("获取表达式结果失败", e);
            }
            return evaluateNumber;
        }
        return evaluate;
    }

    private Map<String, String> getMetricVariables(MetricLibraryVO metricLibraryDetail) {
        Set<String> baseMetricIdsStr = new HashSet<>();
        Collections.addAll(baseMetricIdsStr, metricLibraryDetail.getAllBaseMetricIds().split(","));
        List<MetricLibraryVO> baseMetrics = this.metricLibraryQueryService.getMetricLibraryByCodes(baseMetricIdsStr);
        return baseMetrics.stream()
                .collect(Collectors.toMap(x -> String.valueOf(x.getCode()), MetricLibraryVO::getCode));
    }

    public MetricParam convertParam(CalculateParam param) {
        MetricParam metricParam = new MetricParam();
        metricParam.setRequestId(param.getRequestId());
        metricParam.setAppCode(param.getItemCodeConcat().split("::")[0]);
        metricParam.setPeriod(param.getPeriod());
        return metricParam;
    }
}
