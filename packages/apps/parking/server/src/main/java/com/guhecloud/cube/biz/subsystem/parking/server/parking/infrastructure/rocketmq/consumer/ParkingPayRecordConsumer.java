package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.consumer;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingPayRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingPayRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingPayRecordMessage;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 停车缴费记录消费
 *
 * @author lanchun
 * @date 2022/10/14 15:02
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "parking_pay_msg", topic = ParkingPayRecordMessage.TOPIC)
public class ParkingPayRecordConsumer implements RocketMQListener<ParkingPayRecordMessage> {

    private final ParkingPayRecordService parkingPayRecordService;

    @Override
    public void onMessage(ParkingPayRecordMessage message) {
        ParkingPayRecordPO parkingPayRecordPO = ObjectUtils.copying(message, ParkingPayRecordPO.class);
        this.parkingPayRecordService.save(parkingPayRecordPO);
    }
}

