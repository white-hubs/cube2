package com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message;

import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.MsgEventDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * webSocket发送参数
 *
 * @author lanchun
 * @date 2023/5/11 10:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketSendMessage {

    public static final String TOPIC = "WEB_SITE_SOCKET_SEND";

    /**
     * 发送参数
     */
    private MsgEventDataDTO msgEventDataDTO;

    /**
     * 发送用户集合
     */
    private List<WebSocketSendUser> webSocketSendUsers;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 过滤发送的终端集合
     */
    private Set<String> nonMatchTerminals;

    public WebSocketSendMessage(MsgEventDataDTO msgEventDataDTO, List<WebSocketSendUser> webSocketSendUsers, Integer tenant) {
        this.msgEventDataDTO = msgEventDataDTO;
        this.webSocketSendUsers = webSocketSendUsers;
        this.tenant = tenant;
    }
}

