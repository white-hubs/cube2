package com.guhecloud.cube.base.message2.client.param;

import lombok.Data;

import java.util.Set;

/**
 * 站内广播发送参数
 *
 * @author lanchun
 * @date 2023/10/23 17:06
 */
@Data
public class SatSiteBroadcastSendParam {

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
     * 发送的用户id
     */
    private Set<Long> userIds;

    /**
     * 终端集合
     */
    private Set<String> terminals;

    /**
     * 过滤发送的终端集合
     */
    private Set<String> nonMatchTerminals;
}
