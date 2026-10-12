package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceLifeEventsCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.dao.DeviceLifeEventsMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.event.message.DeviceLifeEventDelMessage;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.event.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceLifeEventsPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceLifeEventsCommandServiceImpl extends ServiceImpl<DeviceLifeEventsMapper, DeviceLifeEventsPO> implements DeviceLifeEventsCommandService {

    @Override
    public void saveOrUpdateEvents(DeviceLifeEventMessage message) {
        List<DeviceLifeEventsPO> pos = this.lambdaQuery()
                .eq(DeviceLifeEventsPO::getEventId, message.getEventId())
                .list();

        if (pos.size() > 0) {
            this.updateEventsPO(pos, message);
        } else {
            this.buildEventsPO(pos, message);
        }

        this.saveOrUpdateBatch(pos);
    }

    @Override
    public void delEvent(DeviceLifeEventDelMessage message) {
        this.lambdaUpdate()
                .set(DeviceLifeEventsPO::getDelState, true)
                .eq(DeviceLifeEventsPO::getEventId, message.getEventId())
                .eq(DeviceLifeEventsPO::getTenant, message.getTenant())
                .update();
    }

    private void updateEventsPO(List<DeviceLifeEventsPO> pos, DeviceLifeEventMessage message) {
        pos.parallelStream().forEach(po -> {
            po.setEventStatus(message.getEventStatus());
            po.setEventTime(message.getEventTime());
        });
    }

    private void buildEventsPO(List<DeviceLifeEventsPO> pos, DeviceLifeEventMessage message) {

        if (message.getTenant() == null) {
            message.setTenant(SessionContextHolder.getRequesterProperty(Requester::getTenant));
        }

        Set<Long> deviceIds = Optional.ofNullable(message.getDeviceIds()).orElse(Collections.emptySet());
        List<DeviceLifeEventsPO> eventsPOS = deviceIds.parallelStream().map(deviceId -> {
            DeviceLifeEventsPO po = new DeviceLifeEventsPO();
            po.setDeviceId(deviceId);
            po.setApplicationCode(message.getApplicationCode());
            po.setApplicationName(message.getApplicationName());
            po.setEventId(message.getEventId());
            po.setEventType(message.getEventType());
            po.setEventTypeName(message.getEventTypeName());
            po.setEventName(message.getEventName());
            po.setEventStatus(message.getEventStatus());
            po.setEventTime(message.getEventTime());
            po.setTenant(message.getTenant());
            po.setProcessInstanceId(message.getProcessInstanceId());
            return po;
        }).collect(Collectors.toList());

        pos.addAll(eventsPOS);
    }
}
