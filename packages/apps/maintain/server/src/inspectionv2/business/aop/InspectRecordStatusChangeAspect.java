package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.aop;

import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author zwb
 */
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordStatusChangeAspect {

    private final ApplicationClient applicationClient;

    @Pointcut("execution(* com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectRecordStatusChangeService.*(..))")
    public void point() {
    }

    @AfterReturning(value = "point()", returning = "res")
    public void publishEvent(JoinPoint joinPoint, Object res) {
        InspectRecordPO po = (InspectRecordPO) res;

        //获取应用
        ApplicationDTO application = applicationClient.getApplicationByCode(MaintainApplicationEnum.INSPECTION.name());

        // 发送 设备生命事件消息
        this.sendDeviceLifeEventMessage(po, application);

    }

    /**
     * 发送 设备生命事件消息
     *
     * @param po 巡检任务
     */
    private void sendDeviceLifeEventMessage(InspectRecordPO po, ApplicationDTO application) {

        if (InspectTargetCategoryEnum.DEVICE.name().equals(po.getTargetCategory())) {

            DeviceLifeEventMessage message = new DeviceLifeEventMessage();

            message.setTenant(po.getTenant());
            message.setDeviceIds(Collections.singleton(po.getTargetId()));
            message.setApplicationCode(application.getCode());
            message.setApplicationName(application.getName());
            message.setEventId(po.getId());
            message.setEventStatus(po.getStatus().getDescription());
            message.setEventName(po.getTaskName());
            message.setEventTime(LocalDateTime.now());
            message.setEventType(MaintainApplicationEnum.INSPECTION.name());
            message.setEventTypeName(MaintainApplicationEnum.INSPECTION.getDescription());

            RocketMQContextHolder.send(DeviceLifeEventMessage.TOPIC, message);
        }

    }

}
