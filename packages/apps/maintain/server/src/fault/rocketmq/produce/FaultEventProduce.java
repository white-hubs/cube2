package com.guhecloud.cube.srv.gen.maintain.server.fault.rocketmq.produce;

import com.google.common.collect.Sets;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ObjTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketSourceTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultOrderEventVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class FaultEventProduce {

    public static void sendFaultEvent(FaultOrderEventVO fault) {
        if (ObjTypeEnum.DEVICE.name().equals(fault.getObjType())) {
            ApplicationClient applicationClient = ApplicationContextHolder.getBean(ApplicationClient.class);
            //获取应用
            ApplicationDTO applicationDTO = applicationClient.getApplicationByCode(MaintainApplicationEnum.FAULT.name());
            DeviceLifeEventMessage deviceLifeEventMessage = new DeviceLifeEventMessage();
            deviceLifeEventMessage.setEventId(fault.getId());
            deviceLifeEventMessage.setProcessInstanceId(fault.getProcessInsId());
            deviceLifeEventMessage.setDeviceIds(Sets.newHashSet(fault.getObjId()));
            deviceLifeEventMessage.setEventStatus(fault.getFaultStatus());
            deviceLifeEventMessage.setEventName(fault.getName());
            deviceLifeEventMessage.setEventTime(LocalDateTime.now());
            deviceLifeEventMessage.setEventType(TicketSourceTypeEnum.FAULT.name());
            deviceLifeEventMessage.setEventTypeName(TicketSourceTypeEnum.FAULT.getDescription());
            deviceLifeEventMessage.setTenant(fault.getTenant());
            deviceLifeEventMessage.setApplicationCode(applicationDTO.getCode());
            deviceLifeEventMessage.setApplicationName(applicationDTO.getName());
            RocketMQContextHolder.send(DeviceLifeEventMessage.TOPIC, deviceLifeEventMessage);
        }
    }
}
