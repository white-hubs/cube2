package com.guhecloud.cube.srv.gen.product.market.server.pricing.util;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExpressionEvaluator {
    public static boolean evaluate(String expression) throws ScriptException {
        return (boolean) new ScriptEngineManager().getEngineByName("JavaScript").eval(expression);
    }
}
