package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.consumer;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingAccessRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingAccessRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingOutRecordMessage;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 停车出场记录消费
 *
 * @author lanchun
 * @date 2022/10/14 15:02
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "parking_out_msg", topic = ParkingOutRecordMessage.TOPIC)
public class ParkingOutRecordConsumer implements RocketMQListener<ParkingOutRecordMessage> {

    private final ParkingAccessRecordService parkingAccessRecordService;

    @Override
    public void onMessage(ParkingOutRecordMessage message) {
        ParkingAccessRecordPO outRecord = ObjectUtils.copying(message, ParkingAccessRecordPO.class);
        outRecord.setAccessType(ParkingAccessTypeEnum.ACCESS_OUT);
        outRecord.setAccessTime(message.getOutTime());
        outRecord.setImageUrl(message.getOutImageUrl());
        outRecord.setImageBase(message.getOutImageBase());
        this.parkingAccessRecordService.save(outRecord);
    }
}

