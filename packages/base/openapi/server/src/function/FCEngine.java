package com.guhecloud.cube.base.openapi.server.function;

import com.guhecloud.cube.base.openapi.server.vo.FunctionMethod;

public interface FCEngine {
    String buildResponse(FunctionMethod functionMethod, String originalResponse);
}
