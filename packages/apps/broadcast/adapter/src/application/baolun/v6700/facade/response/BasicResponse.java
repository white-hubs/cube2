package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BasicResponse {

    /**
     * 结果代码
     */
    @JsonProperty("Ret")
    protected Integer ret;

    /**
     * 简述
     */
    @JsonProperty("Remark")
    protected String remark;
}
