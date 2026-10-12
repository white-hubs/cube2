package com.guhecloud.cube.srv.gen.maintain.server.upkeep.rocketmq.produce;

import com.google.common.collect.Sets;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenaceVo;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MaintainEventProduce {

    public static void sendFaultEvent(MaintenaceVo maintenaceVo) {
        ApplicationClient applicationClient = ApplicationContextHolder.getBean(ApplicationClient.class);
        ApplicationDTO maintenance = applicationClient.getApplicationByCode(MaintainApplicationEnum.MAINTENACE.name());
        DeviceLifeEventMessage deviceLifeEventMessage = new DeviceLifeEventMessage();
        deviceLifeEventMessage.setApplicationCode(maintenance.getCode());
        deviceLifeEventMessage.setApplicationName(maintenance.getName());
        deviceLifeEventMessage.setEventId(maintenaceVo.getId());
        deviceLifeEventMessage.setDeviceIds(Sets.newHashSet(maintenaceVo.getObjId()));
        deviceLifeEventMessage.setEventStatus(maintenaceVo.getStatusName());
        deviceLifeEventMessage.setEventName(maintenaceVo.getName());
        deviceLifeEventMessage.setEventTime(LocalDateTime.now());
        deviceLifeEventMessage.setEventType("UPKEEP");
        deviceLifeEventMessage.setEventTypeName("维保作业");
        deviceLifeEventMessage.setTenant(maintenaceVo.getTenant());
        RocketMQContextHolder.send(DeviceLifeEventMessage.TOPIC, deviceLifeEventMessage);

    }
}
