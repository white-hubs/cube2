package com.guhecloud.cube.srv.gen.reportmanagement.server.rocketmq;

import instance.event.NodePassMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工作会报节点通过消息消费
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = NodePassMessage.TOPIC + "_REPORT_START", topic = NodePassMessage.TOPIC,
                         selectorExpression = "REPORT", selectorType = SelectorType.TAG
)
public class ReportNodePassConsumer implements RocketMQListener<NodePassMessage> {
    @Override
    public void onMessage(NodePassMessage message) {
        log.info("=====>");
    }
}
