package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DeviceNotifyConfigVO {

    /**
     * 通知通道配置
     */
    @NotNull(message = "通知通道不能为空")
    @Size(min = 1, message = "通知通道不能小于1")
    private List<MessageChannelEnum> channelTypes;

    /**
     * 发送通知对象配置
     */
    private List<MessageTargetNotifyVO> targets;
}
