package com.guhecloud.cube.biz.metric.server.metric.calculate.evaluator;

import com.guhecloud.cube.biz.metric.server.metric.calculate.Variable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象表达式计算器
 * 定义计算流程：加载变量值 → 构建上下文 → 执行表达式 → 转换结果
 */
public abstract class AbstractExpressionEvaluator implements ExpressionEvaluator {

    protected final Variable variable;

    public AbstractExpressionEvaluator(Variable variable) {
        this.variable = variable;
    }

    @Override
    public final double evaluate(String formula, Map<String, String> variables, Object param) {
        try {
            if (variables == null || variables.isEmpty()) {
                variables = Collections.emptyMap();
            }
            //执行表达式计算
            Object rawResult = getEvaluate(formula, variables, param);
            //结果转换为double
            return convertResult(rawResult);
        } catch (Exception e) {
            throw new RuntimeException("计算失败: " + formula, e);
        }
    }

    protected Object getEvaluate(String formula, Map<String, String> variables, Object param) {
        Map<String, Object> context = loadVariablesValues(variables, param);
        return doEvaluate(formula, context);
    }

    //1.通过规则id配置
    //2.获取计算指标库中的某个指标
    //3.获取指标库中的公式（与前端约定好 公式中的变量与具体指标的对应关系）
    //4.传入公式进行解析（通过变量找到具体的指标code）
    //5.指标查询得到具体结果
    //6.替换公式里面的变量为指标具体的查询结果
    protected abstract Object doEvaluate(String formula, Map<String, Object> context);

    protected Map<String, Object> loadVariablesValues(Map<String, String> variables, Object param) {
        Map<String, Object> context = new HashMap<>();
        for (String metric : variables.keySet()) {
            String metricCode = variables.get(metric);
            Object value = variable.getVariableValue(metricCode, param);
            context.put(metric, value);
        }
        return context;
    }

    protected double convertResult(Object result) {
        if (result == null) {
            throw new RuntimeException("计算结果为空");
        }
        if (result instanceof Integer) {
            return BigDecimal.valueOf((int) result).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        if (result instanceof Long) {
            return BigDecimal.valueOf((long) result).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        if (result instanceof BigDecimal) {
            return ((BigDecimal) result).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        if (result instanceof Float) {
            return BigDecimal.valueOf((float) result).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        if (result instanceof Double) {
            return BigDecimal.valueOf((double) result).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        throw new RuntimeException("结果无法转为数值: " + result.getClass());
    }
}
