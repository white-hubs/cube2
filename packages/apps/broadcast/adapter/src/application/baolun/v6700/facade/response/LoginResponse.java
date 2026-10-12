package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse extends BasicResponse {

    /**
     * session
     */
    @JsonProperty("JSessionID")
    private String jsessionid;
}
