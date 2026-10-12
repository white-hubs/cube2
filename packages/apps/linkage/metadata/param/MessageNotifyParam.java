package com.guhecloud.apps.linkage.metadata.param;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MessageNotifyParam {

    /**
     * 通知通道配置
     */
    @NotNull(message = "通知通道不能为空")
    @Size(min = 1, message = "通知通道不能小于1")
    private List<MessageChannelEnum> channelTypes;

    /**
     * 通知对象配置
     */
    @NotNull(message = "通知对象不能为空")
    @Size(min = 1, message = "通知对象不能小于1")
    private List<MessageTargetNotifyDTO> targetModifyParams;
}
