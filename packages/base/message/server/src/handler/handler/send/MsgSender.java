package com.guhecloud.cube.base.message2.server.handler.handler.send;

import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;

/**
 * 消息处理器
 *
 * @author lanchun
 * @date 2023/3/13 14:24
 */
public interface MsgSender {

    MessageSendResponse send(MessageInfo messageInfo);
}
