package com.guhecloud.cube.base.flow.webhooks.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * webHook回调
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebHookMessage {
    public static final String TOPIC = "WEB_HOOK_CALL_BACK";
    public static final String GROUP = "WEB_HOOK_GROUP";

    private String processInstanceId;
    private String taskId;
    private Integer tenant;
    private String result;
    private String callBackNode;
}
