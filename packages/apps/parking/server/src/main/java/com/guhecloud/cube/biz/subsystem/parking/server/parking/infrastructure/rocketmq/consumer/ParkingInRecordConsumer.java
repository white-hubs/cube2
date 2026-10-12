package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.consumer;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingAccessRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingAccessRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingInRecordMessage;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration.VehicleNoColorEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleInfoService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 停车入场记录消费
 *
 * @author lanchun
 * @date 2022/10/14 15:02
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "parking_in_msg", topic = ParkingInRecordMessage.TOPIC)
public class ParkingInRecordConsumer implements RocketMQListener<ParkingInRecordMessage> {

    private final ParkingAccessRecordService parkingAccessRecordService;
    private final VehicleInfoService vehicleInfoService;

    @Override
    public void onMessage(ParkingInRecordMessage message) {
        ParkingAccessRecordPO outRecord = ObjectUtils.copying(message, ParkingAccessRecordPO.class);
        outRecord.setAccessType(ParkingAccessTypeEnum.ACCESS_IN);
        outRecord.setAccessTime(message.getInTime());
        outRecord.setImageUrl(message.getInImageUrl());
        outRecord.setImageBase(message.getInImageBase());
        this.parkingAccessRecordService.save(outRecord);
        // 如果车牌号不存在则写入车辆信息；如果存在且车牌颜色为空则更新
        if (StringUtils.isNotBlank(message.getPlateColor())) {
            this.vehicleInfoService.saveOrUpdateVehicleNoColor(message.getVehicleNo(), VehicleNoColorEnum.getColor(message.getPlateColor()));
        }
    }
}

