package com.guhecloud.cube.srv.gen.maintain.server.repair.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.OutsourceWorkOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dto.OutsourceWorkOrderDTO;
import instance.event.NodePassMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 委外维修单节点审批通过消息
 * @date 2024/1/8 16:52
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "OUTSOURCE_ORDER_NODE_PASS", topic = NodePassMessage.TOPIC,
                         selectorExpression = "OUTSOURCE_REPAIR"
)
public class OutsourceWorkOrderNodePassEvent implements RocketMQListener<NodePassMessage> {
    private final OutsourceWorkOrderService outsourceWorkOrderService;

    @Override
    public void onMessage(NodePassMessage message) {
        if (MaintainApplicationEnum.OUTSOURCE_REPAIR.name().equals(message.getBusinessType())) {
            log.info("委外维修单节点审批通过:{},data:{}", message.getProcessInstanceId(), message.getData());
            OutsourceWorkOrderDTO outsourceWorkOrderDTO = JSONObject.parseObject(message.getData(), OutsourceWorkOrderDTO.class);
            outsourceWorkOrderService.updateOutsourceWorkOrder(outsourceWorkOrderDTO, message.getProcessInstanceId());
        }
    }
}
