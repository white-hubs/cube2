package com.guhecloud.cube.biz.metric.server.metric.library.utils;

import com.guhecloud.cube.biz.metric.server.metric.library.enumeration.MetricComputeType;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.MetricLibraryRepository;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.po.MetricLibraryPO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FormulaPreGenerateTool {

    private final MetricLibraryRepository metricRepository;

    public FormulaPreGenerateTool(MetricLibraryRepository metricRepository) {this.metricRepository = metricRepository;}

    /**
     * 生成最终执行表达式（公式模板：带[指标ID]占位符）
     */
    public String resolveFormula(MetricLibraryPO metric) {
        // 1. 递归获取所有基础指标ID和依赖表达式映射
        Set<String> allBaseIds = new HashSet<>();
        Map<String, String> placeholderToExprMap = new HashMap<>(); // 占位符→表达式映射（如"[在线设备数]"→"(metric_1/metric_2)"）
        Set<Long> visitedIds = new HashSet<>(); // 防循环引用
        // 2. 解析当前指标的直接依赖ID集合
        List<Long> directDependencyIds = parseDependencyIds(metric.getComputeFormulaMetrics());
        resolveDependencies(directDependencyIds, metric, placeholderToExprMap, allBaseIds, visitedIds);
        // 3. 替换公式模板中的占位符（支持[名称]和纯ID两种格式）
        String executableFormula = replacePlaceholders(metric.getComputeFormula(), placeholderToExprMap);
        // 4. 保存所有基础指标ID（用于运行时加载数据）
        metric.setAllBaseMetricIds(allBaseIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        return executableFormula;
    }

    /**
     * 递归解析依赖指标，构建占位符→表达式映射
     */
    private void resolveDependencies(List<Long> dependencyIds, MetricLibraryPO currentMetric,
                                     Map<String, String> placeholderMap, Set<String> allBaseIds,
                                     Set<Long> visitedIds
                                    ) {
        for (Long depId : dependencyIds) {
            // 检测循环引用（如789→123→789）
            if (visitedIds.contains(depId)) {
                throw new ServiceException("循环引用：指标ID链=" + visitedIds + "→" + depId);
            }
            visitedIds.add(depId);
            // 查询依赖指标信息
            MetricLibraryPO depMetric = metricRepository.findById(depId);
            // 基础指标：直接添加到allBaseIds，占位符为其ID（或[名称]）
            if (depMetric.getComputeType() != MetricComputeType.COMBINATION_RESULT) {
                allBaseIds.add(depMetric.getCode());
                // 占位符为指标id（[名称]（如"[在线设备数]"）替换为基础指标code，需同时支持
                placeholderMap.put("[" + depId + "]", depMetric.getCode()); // [名称]占位符映射（如"[在线设备数指标id]"→"[123]"）
                visitedIds.remove(depId);
                continue;
            }
            // 组合指标：递归解析其子依赖，并构建当前指标的表达式
            List<Long> childDependencyIds = parseDependencyIds(depMetric.getComputeFormulaMetrics());
            resolveDependencies(childDependencyIds, depMetric, placeholderMap, allBaseIds, visitedIds);
            // 生成当前组合指标的表达式（用其子依赖的表达式替换）
            String depExecutableExpr = replacePlaceholders(depMetric.getExecutableFormula(), placeholderMap);
            depMetric.setExecutableFormula(depExecutableExpr); // 保存子指标的执行表达式
            // 将当前组合指标的占位符（ID或[名称]）映射到其表达式（带括号避免运算优先级问题）
            placeholderMap.put("[" + depMetric.getId() + "]", "(" + depExecutableExpr + ")");
            visitedIds.remove(depId); // 回溯
        }
    }

    /**
     * 解析computeFormulaMetrics为ID列表（逗号分隔字符串→List<Long>）
     */
    private List<Long> parseDependencyIds(String computeFormulaMetrics) {
        if (StringUtils.isBlank(computeFormulaMetrics)) {
            return new ArrayList<>();
        }
        return Arrays.stream(computeFormulaMetrics.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * 替换公式中的占位符（支持两种格式：纯ID和[名称]）
     * 例1："[在线设备数]/[设备总数]*100%" → "(metric_1/metric_2)/metric_456*100"
     * 例2："1/2" → "metric_1/metric_2"
     */
    private String replacePlaceholders(String formulaTemplate, Map<String, String> placeholderMap) {
        String replaced = formulaTemplate;
        // 优先替换[名称]占位符（避免与纯ID混淆，如"[123]"和"123"）
        List<Map.Entry<String, String>> sortedEntries = new ArrayList<>(placeholderMap.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getKey().length() - e1.getKey().length()); // 长key优先替换

        for (Map.Entry<String, String> entry : sortedEntries) {
            String key = entry.getKey();
            replaced = replaced.replace(key, entry.getValue());
        }
        return replaced;
    }
}
