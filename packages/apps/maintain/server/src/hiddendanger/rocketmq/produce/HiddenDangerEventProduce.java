package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.rocketmq.produce;

import com.google.common.collect.Sets;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketSourceTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class HiddenDangerEventProduce {

    public static void sendHiddenDangerEvent(HiddenDangerVO hiddenDangerVO) {
        ApplicationClient applicationClient = ApplicationContextHolder.getBean(ApplicationClient.class);
        ApplicationDTO hiddenDanger = applicationClient.getApplicationByCode(MaintainApplicationEnum.HIDDENDANGER.name());
        DeviceLifeEventMessage deviceLifeEventMessage = new DeviceLifeEventMessage();
        deviceLifeEventMessage.setApplicationCode(hiddenDanger.getCode());
        deviceLifeEventMessage.setApplicationName(hiddenDanger.getName());
        deviceLifeEventMessage.setEventId(hiddenDangerVO.getId());
        deviceLifeEventMessage.setProcessInstanceId(hiddenDangerVO.getProcessInsId());
        deviceLifeEventMessage.setDeviceIds(Sets.newHashSet(hiddenDangerVO.getHiddenObjId()));
        deviceLifeEventMessage.setEventStatus(hiddenDangerVO.getHiddenDangerStatus());
        deviceLifeEventMessage.setEventName(hiddenDangerVO.getName());
        deviceLifeEventMessage.setEventTime(LocalDateTime.now());
        deviceLifeEventMessage.setEventType(TicketSourceTypeEnum.HIDDENDANGER.name());
        deviceLifeEventMessage.setEventTypeName(TicketSourceTypeEnum.HIDDENDANGER.getDescription());
        deviceLifeEventMessage.setTenant(hiddenDangerVO.getTenant());
        RocketMQContextHolder.send(DeviceLifeEventMessage.TOPIC, deviceLifeEventMessage);

    }
}
