package com.guhecloud.cube.srv.gen.maintain.server.repair.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.OutsourceWorkOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dto.OutsourceWorkOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceWorkOrder;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 委外维修单审批结束事件监听
 * @date 2024/1/12 10:26
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "OUTSOURCE_ORDER_COMPLETE", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "OUTSOURCE_REPAIR"
)
public class OutsourceWorkOrderCompleteEvent implements RocketMQListener<ProcessCompletedMessage> {

    private final OutsourceWorkOrderService outsourceWorkOrderService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if (MaintainApplicationEnum.OUTSOURCE_REPAIR.name().equals(message.getBusinessId().getBusinessType())) {
            log.info("委外维修单审批结束:{},data:{}", message.getProcessInstanceId(), message.getData());
            OutsourceWorkOrderDTO outsourceWorkOrderDTO = JSONObject.parseObject(message.getData(), OutsourceWorkOrderDTO.class);
            OutsourceWorkOrder outsourceWorkOrder = outsourceWorkOrderService.updateOutsourceWorkOrder(outsourceWorkOrderDTO, message.getProcessInstanceId());
            outsourceWorkOrderService.sendDeviceLifeEvent(outsourceWorkOrder, message.getResult().getDesc());

        }
    }
}
