package com.guhecloud.cube.biz.metric.server.metric.calculate.evaluator;

import com.guhecloud.cube.biz.metric.server.metric.calculate.Variable;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MvelExpressionEvaluator extends AbstractExpressionEvaluator {

    public final ParserContext mvelContext;

    private final Map<String, Serializable> expressionCache = new ConcurrentHashMap<>();

    public MvelExpressionEvaluator(Variable variable) {
        super(variable);
        this.mvelContext = createParserContext();
    }

    @Override
    public Object doEvaluate(String formula, Map<String, Object> context) {
        if (expressionCache.containsKey(formula.trim())) {
            return MVEL.executeExpression(expressionCache.get(formula.trim()), context);
        } else {
            Serializable compiled = MVEL.compileExpression(formula, mvelContext);
            expressionCache.putIfAbsent(formula.trim(), compiled);
            return MVEL.executeExpression(compiled, context);
        }
    }

    protected ParserContext createParserContext() {
        ParserContext ctx = new ParserContext();
        ctx.setStrictTypeEnforcement(false); // 关闭严格类型检查（避免路径解析问题）
        return ctx;
    }
}
