package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ModifyDeviceNotifyRuleEnabledEvent extends ApplicationEvent {

    private final DeviceNotifyRule deviceNotifyRule;

    public ModifyDeviceNotifyRuleEnabledEvent(Object source, DeviceNotifyRule deviceNotifyRule) {
        super(source);
        this.deviceNotifyRule = deviceNotifyRule;
    }
}
