package com.guhecloud.cube.base.message.sdk.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteBroadcastSendRequest {

    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 业务主体
     */
    private Object businessPrincipal;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 用户id集合
     */
    private Set<Long> userIds;

    /**
     * 终端编号集合
     */
    private Set<String> terminals;

    /**
     * 过滤的终端集合编号
     */
    private Set<String> nonMatchTerminals;
}
