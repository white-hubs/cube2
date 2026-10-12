package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.DateUnitEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 联动消息通知保存参数
 *
 * @author lanchun
 * @date 2022/6/21 17:17
 */
@Data
public class LinkageMessageDTO {

    /**
     * 通知时间类型
     */
    @NotNull(message = "通知时间类型不能为空")
    private MessageNotifyTypeEnum notifyType;

    /**
     * 延迟时间
     */
    private Integer duration;

    /**
     * 延迟时间单位
     */
    private DateUnitEnum delayTimeUnit;

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
