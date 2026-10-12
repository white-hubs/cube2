package com.guhecloud.cube.biz.linkage.client.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RuleTriggerParam {

    /**
     * 触发时间
     */
    private LocalDateTime triggerTime;

    /**
     * 触发事件json
     */
    private Object eventConfig;
}
