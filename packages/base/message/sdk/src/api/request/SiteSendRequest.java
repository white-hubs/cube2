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
public class SiteSendRequest {

    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息事件类型
     */
    private String eventType;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 业务主体
     */
    private Object businessPrincipal;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 发送用户主键集合信息
     */
    private Set<Long> userIds;

    /**
     * 发送终端信息
     */
    private Set<String> terminals;
}
