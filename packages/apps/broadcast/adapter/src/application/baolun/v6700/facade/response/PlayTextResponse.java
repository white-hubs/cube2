package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlayTextResponse extends BasicResponse {

    /**
     * 文本广播任务 ID
     */
    @JsonProperty("TaskUUID")
    private String playTextTaskId;
}
