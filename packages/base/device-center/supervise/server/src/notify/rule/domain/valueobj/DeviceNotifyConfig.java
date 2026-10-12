package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceNotifyConfig {

    /**
     * 通知通道配置
     */
    private List<MessageChannelEnum> channelTypes;

    /**
     * 通知对象配置
     */
    private List<MessageTargetNotify> targets;
}
