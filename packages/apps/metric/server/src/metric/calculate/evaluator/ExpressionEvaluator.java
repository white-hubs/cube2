package com.guhecloud.cube.biz.metric.server.metric.calculate.evaluator;

import java.util.Map;

public interface ExpressionEvaluator {

    double evaluate(String expression, Map<String, String> variables, Object param);
}
