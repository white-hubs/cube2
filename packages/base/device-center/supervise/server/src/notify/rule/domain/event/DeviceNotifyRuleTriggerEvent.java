package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeviceNotifyRuleTriggerEvent extends ApplicationEvent {

    private final DeviceDetailDTO deviceDetailDTO;

    private final String triggerContent;

    private final DeviceNotifyRule deviceNotifyRule;

    public DeviceNotifyRuleTriggerEvent(Object source, DeviceDetailDTO deviceDetailDTO, String triggerContent,
                                        DeviceNotifyRule deviceNotifyRule
                                       ) {
        super(source);
        this.deviceDetailDTO = deviceDetailDTO;
        this.triggerContent = triggerContent;
        this.deviceNotifyRule = deviceNotifyRule;
    }
}
