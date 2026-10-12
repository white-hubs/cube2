package com.guhecloud.cube.base.openapi.metadata.enumeration;

public enum HTTPMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    ;

    private final String method;

    HTTPMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
