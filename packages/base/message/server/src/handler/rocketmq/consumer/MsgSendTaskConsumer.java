package com.guhecloud.cube.base.message2.server.handler.rocketmq.consumer;

import com.guhecloud.cube.base.message2.server.base.service.MsgRecordService;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.pending.MessageSendTask;
import com.guhecloud.cube.base.message2.server.handler.pending.MessageSendTaskPendingHolder;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "msg_send_task", topic = "MSG_SEND_TASK", selectorType = SelectorType.TAG,
                         messageModel = MessageModel.CLUSTERING
)
public class MsgSendTaskConsumer implements RocketMQListener<MessageInfo> {

    private final MessageSendTaskPendingHolder messageSendTaskPendingHolder;

    private final MsgRecordService msgRecordService;

    @Override
    public void onMessage(MessageInfo message) {
        //10分钟内如果还未成功发出则直接标记失败
        if ((System.currentTimeMillis() - message.getTimestamp()) > 60 * 1000 * 10) {
            log.error("消息任务为{}已发送超时,10分钟内未成功发出,不再进行发送", message.getId());
            //发送失败入库
            this.msgRecordService.updateMsgRecordStatus(message.getId(),
                                                        false, DateUtils.now(),
                                                        message.getMessageReceiverInfos().stream().map(messageReceiverInfo -> {
                                                            return new MessageSendResponse.Receiver(messageReceiverInfo.getUserId()
                                                                                                            .toString(), "send_timeout");
                                                        }).collect(Collectors.toList()));
        } else {
            MessageSendTask messageSendTask = ApplicationContextHolder.getBean(MessageSendTask.class).setMessageInfo(message);
            //将任务提交到线程池中
            this.messageSendTaskPendingHolder.route(message.getChannelCode().name()).submit(messageSendTask);
        }
    }
}
