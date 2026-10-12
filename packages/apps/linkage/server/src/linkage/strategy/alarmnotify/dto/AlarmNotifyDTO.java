package com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.dto;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlarmNotifyDTO {

    /**
     * 是否仅指派给当班人员
     */
    private Boolean dutyUser;

    /**
     * 通知配置
     */
    private List<MessageTargetNotifyDTO> targetModifyParams;

    /**
     * 通知通道配置
     */
    private List<MessageChannelEnum> channelTypes;
}
