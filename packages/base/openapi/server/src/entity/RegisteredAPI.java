package com.guhecloud.cube.base.openapi.server.entity;

import com.guhecloud.cube.base.openapi.metadata.enumeration.HTTPMethod;
import com.guhecloud.cube.base.openapi.metadata.enumeration.ResponseType;
import com.guhecloud.cube.base.openapi.server.vo.FunctionMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisteredAPI {
    private String name;
    private String path;
    /**
     * 不配置 method 则表示支持所有方法
     */
    private HTTPMethod method;
    private String description;
    private String appCode;
    private String targetURL;
    private HTTPMethod targetMethod;
    /**
     * 响应类型为非 text 类型时，不支持 afterFC
     */
    private ResponseType responseType;
    private FunctionMethod beforeFC;
    private FunctionMethod afterFC;

    public boolean hasBeforeFC() {
        return beforeFC != null && beforeFC.classpath() != null && !beforeFC.classpath().isEmpty()
               && beforeFC.methodName() != null && !beforeFC.methodName().isEmpty();
    }

    public boolean hasAfterFC() {
        return afterFC != null && afterFC.classpath() != null && !afterFC.classpath().isEmpty()
               && afterFC.methodName() != null && !afterFC.methodName().isEmpty();
    }
}
