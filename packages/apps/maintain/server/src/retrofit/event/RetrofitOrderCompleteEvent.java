package com.guhecloud.cube.srv.gen.maintain.server.retrofit.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.commandgateway.RetrofitOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.dto.RetrofitOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.po.RetrofitOrder;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 更新改造申请单审批结束消息
 * @date 2024/1/12 10:55
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "RETROFIT_ORDER_COMPLETE", topic = ProcessCompletedMessage.TOPIC, selectorExpression = "RETROFIT")
public class RetrofitOrderCompleteEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final RetrofitOrderService retrofitOrderService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if (MaintainApplicationEnum.RETROFIT.name().equals(message.getBusinessId().getBusinessType())) {
            log.info("更新改造单审批结束:{},data:{}", message.getProcessInstanceId(), message.getData());
            RetrofitOrderDTO retrofitOrderDTO = JSONObject.parseObject(message.getData(), RetrofitOrderDTO.class);
            RetrofitOrder retrofitOrder = retrofitOrderService.updateRetrofitOrder(retrofitOrderDTO, message.getProcessInstanceId());
            retrofitOrderService.sendDeviceLifeEvent(retrofitOrder, message.getResult().getDesc());
        }
    }
}
