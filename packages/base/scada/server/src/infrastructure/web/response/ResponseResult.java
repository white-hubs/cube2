package com.guhecloud.kaihe.scada.server.infrastructure.web.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ResponseResult {

    private Object data;
    private Boolean success;
    private String message;
    private Map<String,String> errors;

}
