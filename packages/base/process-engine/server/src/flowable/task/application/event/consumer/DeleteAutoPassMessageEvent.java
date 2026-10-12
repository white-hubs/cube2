package com.guhecloud.cube.base.flow.flowable.task.application.event.consumer;

import com.guhecloud.cube.base.flow.flowable.common.resovler.AutoPassMessageTopic;
import com.guhecloud.cube.base.flow.flowable.task.application.event.publish.DeleteTaskTodoMessage;
import com.guhecloud.cube.base.flow.flowable.task.domain.valueobject.AutoPassTodoMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 570
 * @createTime: 2023-07-02 9:31
 * @description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "DELETE_AUTO_PASS_MESSAGE", topic = AutoPassMessageTopic.TOPIC)
public class DeleteAutoPassMessageEvent implements RocketMQListener<AutoPassTodoMessage> {

    private final DeleteTaskTodoMessage deleteTaskTodoMessage;

    @Override
    public void onMessage(AutoPassTodoMessage message) {
        log.info("删除自动通过或签节点的其他待办消息");
        deleteTaskTodoMessage.deleteOtherCountersignTask(message);
    }
}
