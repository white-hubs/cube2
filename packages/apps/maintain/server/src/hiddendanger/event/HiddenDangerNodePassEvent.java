package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.event;

import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import instance.event.NodePassMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 隐患单节点审批通过消息
 * @date 2023/11/9 9:48
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "HIDDEN_DANGER_NODE_PASS", topic = NodePassMessage.TOPIC, selectorExpression = "HIDDENDANGER")
public class HiddenDangerNodePassEvent implements RocketMQListener<NodePassMessage> {
    private final HiddenDangerNewService hiddenDangerNewService;

    @Override
    public void onMessage(NodePassMessage message) {
        if (MaintainApplicationEnum.HIDDENDANGER.name().equals(message.getBusinessType())) {
            log.info("隐患排查节点审批通过:{},data:{}", message.getProcessInstanceId(), message.getData());
            hiddenDangerNewService.updateHiddenDanger(message);
        }
    }
}
