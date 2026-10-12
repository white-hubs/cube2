package com.guhecloud.cube.base.openapi.server.entity;

import com.guhecloud.cube.base.openapi.metadata.enumeration.FCEngineType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FunctionDefinition {
    private String classpath;
    private String description;
    private FCEngineType FCEngineType;
    private String fileName;
    private String sourceCode;
}
