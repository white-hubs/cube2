package com.guhecloud.cube.base.openapi.server.function.engine.js;

import com.guhecloud.cube.base.openapi.server.function.engine.ExecutorEngine;

public class JavaScriptExecutorEngine implements ExecutorEngine {

    @Override
    public Object call(String classpath, String methodName, Class<?>[] parameterTypes, Object[] args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
