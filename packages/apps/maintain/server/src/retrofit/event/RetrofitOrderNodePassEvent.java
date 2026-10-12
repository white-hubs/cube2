package com.guhecloud.cube.srv.gen.maintain.server.retrofit.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.commandgateway.RetrofitOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.dto.RetrofitOrderDTO;
import instance.event.NodePassMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新改造单2.0节点审批通过消息
 *
 * @author lanchun
 * @date 2024/1/8 16:12
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "RETROFIT_ORDER_NODE_PASS", topic = NodePassMessage.TOPIC, selectorExpression = "RETROFIT")
public class RetrofitOrderNodePassEvent implements RocketMQListener<NodePassMessage> {
    private final RetrofitOrderService retrofitOrderService;

    @Override
    public void onMessage(NodePassMessage message) {
        if (MaintainApplicationEnum.RETROFIT.name().equals(message.getBusinessType())) {
            log.info("更新改造单节点审批通过:{},data:{}", message.getProcessInstanceId(), message.getData());
            final RetrofitOrderDTO retrofitOrderDTO = JSONObject.parseObject(message.getData(), RetrofitOrderDTO.class);
            retrofitOrderService.updateRetrofitOrder(retrofitOrderDTO, message.getProcessInstanceId());
        }
    }
}
