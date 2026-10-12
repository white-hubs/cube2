package com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerWebsocketDTO implements Serializable {

    private String sessionId;

    private String webSocketUserId;

    /**
     * 订阅端
     */
    private String terminal;

    /**
     * 订阅类型
     */
    private String subscribeType;
}
