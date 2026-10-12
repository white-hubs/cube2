package com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * webSocket推送所需用户信息
 *
 * @author lanchun
 * @date 2023/5/11 10:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketSendUser {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 推送终端编号
     */
    private String terminal;
}

