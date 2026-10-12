package com.guhecloud.cube.base.openapi.server.function.engine;

public interface ExecutorEngine {
    Object call(String classpath, String methodName, Class<?>[] parameterTypes, Object[] args);
}
