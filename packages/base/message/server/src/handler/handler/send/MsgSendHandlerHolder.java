package com.guhecloud.cube.base.message2.server.handler.handler.send;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 负责通道与处理器的映射
 *
 * @author lanchun
 * @date 2023/3/13 14:45
 */
@Component
public class MsgSendHandlerHolder {

    private Map<ChannelEnum, MsgSender> handlerMap = new HashMap<>();

    public void putHandler(ChannelEnum channelCode, MsgSender handler) {
        this.handlerMap.put(channelCode, handler);
    }

    public MsgSender getHandler(ChannelEnum channelCode) {
        return this.handlerMap.get(channelCode);
    }
}
