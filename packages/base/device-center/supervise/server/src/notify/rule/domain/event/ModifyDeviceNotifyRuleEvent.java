package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ModifyDeviceNotifyRuleEvent extends ApplicationEvent {

    private final Long oldIotRuleId;

    private final DeviceNotifyRule deviceNotifyRule;

    public ModifyDeviceNotifyRuleEvent(Object source, Long oldIotRuleId, DeviceNotifyRule deviceNotifyRule) {
        super(source);
        this.oldIotRuleId = oldIotRuleId;
        this.deviceNotifyRule = deviceNotifyRule;
    }
}
