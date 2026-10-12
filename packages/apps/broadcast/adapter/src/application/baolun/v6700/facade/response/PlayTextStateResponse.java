package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlayTextStateResponse extends BasicResponse {

    /**
     * 文本广播播放状态
     * <p>
     * 0 - 等待文本转语音
     * 1 - 正在进行文本转语音
     * 2 - 播放语音中
     * 3 - 文本广播失败
     * 4 - 文本广播结束
     */
    @JsonProperty("State")
    private Integer state;
}
