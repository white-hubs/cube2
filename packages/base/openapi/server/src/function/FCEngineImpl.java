package com.guhecloud.cube.base.openapi.server.function;

import com.guhecloud.cube.base.openapi.server.function.engine.java.JavaExecutorEngine;
import com.guhecloud.cube.base.openapi.server.vo.FunctionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FCEngineImpl implements FCEngine {
    private final JavaExecutorEngine javaExecutorEngine;

    public FCEngineImpl(JavaExecutorEngine javaExecutorEngine) {
        this.javaExecutorEngine = javaExecutorEngine;
    }

    public String buildResponse(FunctionMethod functionMethod, String originalResponse) {
        // TODO: 这里缺少一个 fc executor 判断，后面补充
        return buildJavaResponse(functionMethod, originalResponse);
    }

    private String buildJavaResponse(FunctionMethod functionMethod, String originalResponse) {
        return (String) javaExecutorEngine.call(functionMethod.classpath(), functionMethod.methodName(), new Class[]{
                String.class
        }, new Object[]{
                originalResponse
        });
    }
}
