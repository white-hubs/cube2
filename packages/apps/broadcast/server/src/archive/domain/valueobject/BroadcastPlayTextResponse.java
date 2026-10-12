package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject;

import lombok.Data;

@Data
public class BroadcastPlayTextResponse {

    /**
     * 文本播放任务id
     */
    private String playTextTaskId;

    /**
     * 播放结果
     */
    private String broadcastPlayStatus;

    /**
     * 播放异常描述
     */
    private String broadcastPlayErrorDesc;
}
