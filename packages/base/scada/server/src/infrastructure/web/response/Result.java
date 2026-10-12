package com.guhecloud.kaihe.scada.server.infrastructure.web.response;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Result {

    private Object data;
    private String message;
    private Boolean success;
    private Long count;
    @Builder.Default
    private Map<String, Object> map = new HashMap<>();

    public Result prop(String name, Object value) {
        map.put(name, value);
        return this;
    }

}
