package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.constant.TransferTopicConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.service.DataTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 市场消费集团MQ的消息
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnBean(name = "transferRocketMQProperties")
public class MarketTaskConsumer implements MessageListenerOrderly {

    private final DataTransferService dataTransferService;

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt msg : msgs) {
            String messageStr = new String(msg.getBody());
            try {
                JSONObject jsonObject = JSON.parseObject(messageStr);
                String payload = jsonObject.getString("payload");
                if (TransferTopicConstant.CULTIVATE_TASK_TOPIC.equals(msg.getTopic())) {

                    return dataTransferService.trainingTaskDataTransfer(payload);

                } else if (TransferTopicConstant.EXAM_TASK_TOPIC.equals(msg.getTopic())) {

                    return dataTransferService.examTaskDataTransfer(payload);

                }
            } catch (Exception ex) {
                // 异常处理逻辑
                log.error("市场消息消费失败,失败原因:{}", ex);
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
