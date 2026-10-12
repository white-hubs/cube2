package com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.event;

import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.service.HotelReservationOrderService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.ProcessCompletedMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/28 13:42
 * @Description: 酒店预约完成消费
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "HOTEL_RESERVATION_COMPLETE_SYNC",
                         topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "HOTEL_RESERVATION"
)
public class HotelReservationCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final HotelReservationOrderService hotelReservationOrderService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            hotelReservationOrderService.createHotelReservationOrder(message);
        } catch (Exception e) {
            log.error("HotelReservationCompletedConsumer.onMessage: ", e);
        }
    }
}
