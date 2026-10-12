package com.guhecloud.cube.base.message2.server.handler.pending;

import com.guhecloud.cube.base.message2.server.handler.handler.send.MsgSendHandlerHolder;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 消息发送任务
 * 2.发送消息
 *
 * @author lanchun
 * @date 2023/3/13 16:06
 */
@Data
@Accessors(chain = true)
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MessageSendTask implements Runnable {

    @Autowired
    private MsgSendHandlerHolder msgSendHandlerHolder;

    private MessageInfo messageInfo;

    @Override
    public void run() {
        //send
        msgSendHandlerHolder.getHandler(messageInfo.getChannelCode()).send(messageInfo);
    }
}
